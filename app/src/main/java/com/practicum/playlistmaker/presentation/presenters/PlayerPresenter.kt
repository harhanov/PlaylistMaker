package com.practicum.playlistmaker.presentation.presenters

import com.practicum.playlistmaker.data.mediaplayer.MediaPlayer
import com.practicum.playlistmaker.domain.interactors.PlayerInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.utils.DateUtils.formatTrackTime
import com.practicum.playlistmaker.ui.player.PlayerView

class PlayerPresenter(track: Track, private val playerView: PlayerView) {
    private val mediaPlayer: PlayerInteractor = MediaPlayer(track)
    private var playerState = PlayerState.DEFAULT

    init {
        preparePlayer()
        setOnCompletionListener()
    }

    private fun start() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
        playerView.setPlayButtonIcon(true)
        playerView.startPostDelay()
    }

    private fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSED
        playerView.setPlayButtonIcon(false)
        playerView.removePostDelay()
    }

    fun stopPlayback() {
        if (isPlaybackPlaying()) {
            mediaPlayer.onDestroy()
            playerState = PlayerState.PREPARED
            playerView.setPlayButtonIcon(false)
            playerView.onCompletePlaying()
        }
    }

    fun getCurrentPosition(): String {
        return formatTrackTime(mediaPlayer.getCurrentTime().toString())

    }

    private fun setOnCompletionListener() {
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.PREPARED
            playerView.setPlayButtonIcon(true)
            playerView.onCompletePlaying()
        }
    }

    private fun preparePlayer() {
        mediaPlayer.preparePlayer {
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