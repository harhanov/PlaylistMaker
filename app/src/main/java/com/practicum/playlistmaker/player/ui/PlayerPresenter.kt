package com.practicum.playlistmaker.ui.presenters

import com.practicum.playlistmaker.search.domain.Track
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.ui.PlayerView
import com.practicum.playlistmaker.utils.DateUtils.formatTrackTime

class PlayerPresenter(track: Track, private val playerView: PlayerView) {
    private val playerInteractorImpl: PlayerInteractor = PlayerInteractorImpl(track)
    private var playerState = PlayerState.DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private val timerUpdater = object : Runnable {
        override fun run() {
            playerView.updateTimer(getCurrentPosition())
            handler.postDelayed(
                this,
                CURRENT_POSITION_REFRESH,
            )
        }
    }

    init {
        preparePlayer()
        setOnCompletionListener()
    }

    fun startPostDelay() {
        handler.postDelayed(timerUpdater, CURRENT_POSITION_REFRESH)
    }

    private fun start() {
        playerInteractorImpl.start()
        playerState = PlayerState.PLAYING
        playerView.setPlayButtonIcon(true)
        playerView.startPostDelay()
    }

    private fun pause() {
        playerInteractorImpl.pause()
        playerState = PlayerState.PAUSED
        playerView.setPlayButtonIcon(false)
    }

    fun stopPlayback() {
        if (isPlaybackPlaying()) {
            playerInteractorImpl.onDestroy()
            playerState = PlayerState.PREPARED
            playerView.setPlayButtonIcon(false)
            playerView.onCompletePlaying()
        }
    }

    fun releasePlayer() {
        stopPlayback()
        removePostDelay()
        playerInteractorImpl.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    fun getCurrentPosition(): String {
        return formatTrackTime(playerInteractorImpl.getCurrentTime().toString())

    }

    private fun setOnCompletionListener() {
        playerInteractorImpl.setOnCompletionListener {
            playerState = PlayerState.PREPARED
            playerView.setPlayButtonIcon(true)
            playerView.onCompletePlaying()
            removePostDelay()
            playerView.setPlayButtonIcon(false)
        }
    }

    private fun removePostDelay() {
        handler.removeCallbacks(timerUpdater)
    }

    private fun preparePlayer() {
        playerInteractorImpl.preparePlayer {
            playerView.setPlayButtonIcon(false)
            playerState = PlayerState.PREPARED
        }
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

    private fun isPlaybackPlaying(): Boolean {
        return playerState == PlayerState.PLAYING
    }

    companion object {
        private const val CURRENT_POSITION_REFRESH = 200L
    }
}

enum class PlayerState {
    DEFAULT, PREPARED, PLAYING, PAUSED
}