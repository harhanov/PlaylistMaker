package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.TrackModel

class MediaPlayerControl(private val mediaPlayer: MediaPlayer) : PlayerControl {

    override fun preparePlayer(track: TrackModel, prepare: () -> Unit) {
        mediaPlayer.setOnPreparedListener { prepare() }
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
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