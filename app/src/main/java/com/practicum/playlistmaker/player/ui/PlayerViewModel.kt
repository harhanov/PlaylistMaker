package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.TrackModel
import com.practicum.playlistmaker.utils.DateUtils.formatTrackTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class PlayerViewModel(private val trackModel: TrackModel) : ViewModel(), KoinComponent {

    private val _screenState = MutableLiveData<PlayerScreenState>()
    val screenState: LiveData<PlayerScreenState> = _screenState

    private val playerInteractor: PlayerInteractor by inject()
    private var playerState = PlayerState.DEFAULT

    private var timerJob: Job? = null

    init {
        val initialPosition = formatTrackTime(playerInteractor.getCurrentTime().toString())
        _screenState.value = PlayerScreenState.BeginningState(trackModel, initialPosition)
        preparePlayer()
        setOnCompletionListener()
    }

    private fun preparePlayer() {
        playerInteractor.preparePlayer(trackModel) {
            playerState = PlayerState.PREPARED
            _screenState.value = PlayerScreenState.Preparing()
        }
    }

    private fun setOnCompletionListener() {
        playerInteractor.setOnCompletionListener {
            playerState = PlayerState.PREPARED
            _screenState.value = PlayerScreenState.OnCompletePlaying()
        }
    }

    private fun start() {
        playerInteractor.start()
        playerState = PlayerState.PLAYING
        startTimer()
    }

    fun pause() {
        playerInteractor.pause()
        playerState = PlayerState.PAUSED
        timerJob?.cancel()
        _screenState.value = PlayerScreenState.PlayButtonHandling(playerState)
    }

    private fun updateTimer() {
        val time = formatTrackTime(playerInteractor.getCurrentTime().toString())
        val currentState = PlayerScreenState.BeginningState(trackModel, time)
        updatePlayerState(currentState)
    }

    fun onDestroy() {
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

        _screenState.value = PlayerScreenState.PlayButtonHandling(playerState)
    }

    companion object {
        private const val CURRENT_POSITION_REFRESH = 300L
    }
}

enum class PlayerState {
    DEFAULT, PREPARED, PLAYING, PAUSED
}