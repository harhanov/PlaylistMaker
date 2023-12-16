package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_library.favourites.domain.FavouritesInteractor
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel
import com.practicum.playlistmaker.media_library.playlists.ui.PlaylistsState
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.TrackModel
import com.practicum.playlistmaker.player.ui.PlayerScreenState.*
import com.practicum.playlistmaker.utils.DateUtils.formatTrackTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlayerViewModel(
    private val trackModel: TrackModel,
    private val favouritesInteractor: FavouritesInteractor,
    val playlistInteractor: PlaylistInteractor,
) : ViewModel(), KoinComponent {

    private var isFavourite = trackModel.isFavourite
    private val _screenState = MutableLiveData<PlayerScreenState>()
    val screenState: LiveData<PlayerScreenState> = _screenState

    private val playerInteractor: PlayerInteractor by inject()
    private var playerState = PlayerState.DEFAULT
    private var timerJob: Job? = null

    private var bottomSheetState: BottomSheetState = BottomSheetState.DEFAULT

    private val _playlistsState = MutableLiveData<PlaylistsState>()
    val playlistsState: LiveData<PlaylistsState> = _playlistsState

    private val _playerEvent = MutableLiveData<PlayerEvent>()
    val playerEvent: LiveData<PlayerEvent> get() = _playerEvent

    private fun navigateBackToPlayerFragment() {
        _playerEvent.postValue(PlayerEvent.NavigateBackToPlayerFragment)
    }

    init {
        val initialPosition = formatTrackTime(playerInteractor.getCurrentTime().toString())
        _screenState.value = BeginningState(trackModel, initialPosition)
        preparePlayer()
        setOnCompletionListener()
    }

    fun preparePlayer() {
        playerInteractor.preparePlayer(trackModel) {
            playerState = PlayerState.PREPARED
            updateFavoriteButtonState(trackModel.isFavourite)
            _screenState.value = Preparing(trackModel)
        }
        updateTimer()
    }

    private fun setOnCompletionListener() {
        playerInteractor.setOnCompletionListener {
            playerState = PlayerState.PREPARED
            _screenState.value = OnCompletePlaying
        }
    }

    private fun start() {
        playerInteractor.start()
        playerState = PlayerState.PLAYING
        startTimer()
    }

    private fun pause() {
        playerInteractor.pause()
        playerState = PlayerState.PAUSED
        timerJob?.cancel()
        _screenState.value = PlayButtonHandling(playerState)
    }

    private fun updateTimer() {
        val time = formatTrackTime(playerInteractor.getCurrentTime().toString())
        val currentState = BeginningState(trackModel, time)
        updatePlayerState(currentState)
    }

    fun onDestroy() {
        timerJob?.cancel()
        playerInteractor.onDestroy()
        viewModelScope.cancel()
    }

    fun playbackControl() {
        when (playerState) {
            PlayerState.PLAYING -> {
                pause()
            }

            PlayerState.PREPARED, PlayerState.PAUSED -> {
                start()
            }

            PlayerState.DEFAULT -> {

            }
        }
    }

    private fun updatePlayerState(newState: PlayerScreenState) {
        _screenState.postValue(newState)
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerState == PlayerState.PLAYING) {
                updateTimer()
                delay(CURRENT_POSITION_REFRESH)
            }
        }
        _screenState.value = PlayButtonHandling(playerState)
    }

    suspend fun onFavoriteClicked() {
        isFavourite = !isFavourite
        updateFavoriteButtonState(isFavourite)
        if (isFavourite) {
            favouritesInteractor.addFavourite(trackModel)
        } else {
            favouritesInteractor.removeFavourite(trackModel)
        }
    }

    fun toggleBottomSheetVisibility() {
        bottomSheetState = bottomSheetState.toggle() as BottomSheetState
        _screenState.value = ShowPlaylistBottomSheet(bottomSheetState)
    }

    private fun updateFavoriteButtonState(isFavourite: Boolean) {
        _screenState.value = FavouritesButtonHandling(isFavourite)
    }

    fun updatePlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlists ->
                if (playlists.isNotEmpty()) {
                    _playlistsState.value = PlaylistsState.PlaylistsLoaded(playlists)
                } else {
                    _playlistsState.value = PlaylistsState.Empty
                }
            }
        }
    }

    fun onPlaylistItemClick(playlist: PlaylistModel) {
        viewModelScope.launch {
            val trackId = trackModel.trackId
            val isTrackInPlaylist =
                playlist.playlistId?.let { playlistInteractor.isTrackInPlaylist(it, trackId) }
            if (!isTrackInPlaylist!!) {
                playlist.playlistId.let { playlistInteractor.addTrackToPlaylist(it, trackId) }
                _screenState.value = playlist.playlistName?.let { TrackAddedToPlaylist(it) }
                navigateBackToPlayerFragment()
            } else {
                _screenState.value = playlist.playlistName?.let { TrackAlreadyInPlaylist(it) }
            }
        }
    }

    companion object {
        private const val CURRENT_POSITION_REFRESH = 300L
    }
}

enum class PlayerState {
    DEFAULT, PREPARED, PLAYING, PAUSED
}