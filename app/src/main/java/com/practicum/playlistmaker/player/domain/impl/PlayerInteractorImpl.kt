package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.search.domain.Track
import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.PlayerInteractor


class PlayerInteractorImpl (track: Track): PlayerInteractor {

    private val mediaPlayer: MediaPlayer = MediaPlayer()
    init {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        mediaPlayer.release()
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            onComplete()
        }
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }

    override fun preparePlayer(prepare: () -> Unit) {
        mediaPlayer.setOnPreparedListener { prepare() }
    }
}