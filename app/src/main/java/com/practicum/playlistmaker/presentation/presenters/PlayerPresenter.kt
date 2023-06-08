package com.practicum.playlistmaker.presentation.presenters

import com.practicum.playlistmaker.data.mediaplayer.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.interactors.PlayerInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.utils.DateUtils.formatTrackTime
import com.practicum.playlistmaker.presentation.ui.player.PlayerView

class PlayerPresenter(track: Track, private val playerView: PlayerView) {
    private val playerInteractorImpl: PlayerInteractor = PlayerInteractorImpl(track)
    private var playerState = PlayerState.DEFAULT

    init {
        preparePlayer()
        setOnCompletionListener()
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
        playerView.removePostDelay()
    }

    fun stopPlayback() {
        if (isPlaybackPlaying()) {
            playerInteractorImpl.onDestroy()
            playerState = PlayerState.PREPARED
            playerView.setPlayButtonIcon(false)
            playerView.onCompletePlaying()
        }
    }

    fun getCurrentPosition(): String {
        return formatTrackTime(playerInteractorImpl.getCurrentTime().toString())

    }

    private fun setOnCompletionListener() {
        playerInteractorImpl.setOnCompletionListener {
            playerState = PlayerState.PREPARED
            playerView.setPlayButtonIcon(true)
            playerView.onCompletePlaying()
        }
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
}

enum class PlayerState {
    DEFAULT, PREPARED, PLAYING, PAUSED
}