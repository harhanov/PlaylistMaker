package com.practicum.playlistmaker.player.data

import com.practicum.playlistmaker.player.domain.TrackModel

interface PlayerControl {
    fun preparePlayer(track: TrackModel, prepare: () -> Unit)

    fun setOnCompletionListener(onComplete: () -> Unit)

    fun start()

    fun pause()

    fun onDestroy()

    fun getCurrentTime(): Int

}