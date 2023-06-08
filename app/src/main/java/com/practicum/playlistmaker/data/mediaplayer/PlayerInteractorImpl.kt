package com.practicum.playlistmaker.data.mediaplayer

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.interactors.PlayerInteractor
import com.practicum.playlistmaker.domain.models.Track

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

    override fun preparePlayer(onPrepared: () -> Unit) {
        mediaPlayer.setOnPreparedListener { onPrepared() }
    }
}