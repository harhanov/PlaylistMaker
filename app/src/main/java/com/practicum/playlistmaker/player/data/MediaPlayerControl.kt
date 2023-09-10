package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.search.data.model.Track

class MediaPlayerControl(track: Track): PlayerControl {

    private val mediaPlayer: MediaPlayer = MediaPlayer()

    init {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
    }

    override fun preparePlayer(prepare: () -> Unit) {
        mediaPlayer.setOnPreparedListener { prepare() }
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            onComplete()
        }
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

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }
}