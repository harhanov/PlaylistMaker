package com.practicum.playlistmaker.player.ui


import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.data.model.Track
import com.practicum.playlistmaker.utils.DateUtils.formatTrackTime


class PlayerViewModel(trackForPlayer: Track) : ViewModel() {


    private val _screenState = MutableLiveData<PlayerScreenState>()
    val screenState: LiveData<PlayerScreenState> = _screenState

    private val playerInteractor = Creator.providePlayerInteractor(trackForPlayer)
    private var playerState = PlayerState.DEFAULT
    private val handler: Handler = Handler(Looper.getMainLooper())

    private val timerUpdater =
        object : Runnable {
            override fun run() {
                updateTimer(getCurrentPosition())
                handler.postDelayed(this, CURRENT_POSITION_REFRESH)
            }
        }

    init {
        _screenState.value = PlayerScreenState.BeginningState(trackForPlayer)
        preparePlayer()
        setOnCompletionListener()
    }

    private fun preparePlayer() {
        playerInteractor.preparePlayer {
            playerState = PlayerState.PREPARED
            _screenState.value = PlayerScreenState.Preparing()
        }
    }

    private fun setOnCompletionListener() {
        playerInteractor.setOnCompletionListener {
            playerState = PlayerState.PREPARED
            handler.removeCallbacks(timerUpdater)
            _screenState.value = PlayerScreenState.onCompletePlaying()
        }
    }

    private fun start() {
        playerInteractor.start()
        playerState = PlayerState.PLAYING
        handler.postDelayed(timerUpdater, CURRENT_POSITION_REFRESH)
        _screenState.value = PlayerScreenState.PlayButtonHandling(playerState)
    }

    fun pause() {
        playerInteractor.pause()
        playerState = PlayerState.PAUSED
        handler.removeCallbacks(timerUpdater)
        _screenState.value = PlayerScreenState.PlayButtonHandling(playerState)
    }

    private fun updatePlayerState(newState: PlayerScreenState) {
        _screenState.postValue(newState)
    }

    private fun updateTimer(time: String) {
        updatePlayerState(PlayerScreenState.updateTimer(time))
    }

    fun getCurrentPosition(): String {
        return formatTrackTime(playerInteractor.getCurrentTime().toString())
    }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        playerInteractor.onDestroy()
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

    companion object {
        private const val CURRENT_POSITION_REFRESH = 200L
        private val SEARCH_REQUEST_TOKEN = Any()
        fun getViewModelFactory(trackForPlayer: Track): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        trackForPlayer
                    ) as T
                }
            }
    }
}

enum class PlayerState {
    DEFAULT, PREPARED, PLAYING, PAUSED
}