package com.practicum.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.TrackModel
import com.practicum.playlistmaker.utils.DateUtils.formatTrackTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class PlayerViewModel(private val trackModel: TrackModel) : ViewModel(), KoinComponent {

    private val _screenState = MutableLiveData<PlayerScreenState>()
    val screenState: LiveData<PlayerScreenState> = _screenState

    private val playerInteractor: PlayerInteractor by inject()
    private var playerState = PlayerState.DEFAULT
    private val handler: Handler = Handler(Looper.getMainLooper())

    private val timerUpdater = object : Runnable {
        override fun run() {
            updateTimer()
            handler.postDelayed(this, CURRENT_POSITION_REFRESH)
        }
    }

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
            handler.removeCallbacks(timerUpdater)
            _screenState.value = PlayerScreenState.OnCompletePlaying()
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

    private fun updateTimer() {
        val time = formatTrackTime(playerInteractor.getCurrentTime().toString())
        val currentState = PlayerScreenState.BeginningState(trackModel, time)
        updatePlayerState(currentState)
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

    private fun updatePlayerState(newState: PlayerScreenState) {
        _screenState.postValue(newState)
    }

    companion object {
        private const val CURRENT_POSITION_REFRESH = 200L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}

enum class PlayerState {
    DEFAULT, PREPARED, PLAYING, PAUSED
}