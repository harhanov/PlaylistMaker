package com.practicum.playlistmaker.presentation.ui.player


interface PlayerView {

    fun updateTimer(timerValue: String)

    fun setPlayButtonIcon(isPlaying: Boolean)

    fun onCompletePlaying()

    fun removePostDelay()

    fun startPostDelay()

}