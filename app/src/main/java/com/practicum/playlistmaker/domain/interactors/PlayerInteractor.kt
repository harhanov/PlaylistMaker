package com.practicum.playlistmaker.domain.interactors

interface PlayerInteractor {
    fun start()
    fun pause()
    fun onDestroy()
    fun setOnCompletionListener(onComplete: ()->Unit)
    fun getCurrentTime(): Int
    fun preparePlayer(prepare: () -> Unit)
}