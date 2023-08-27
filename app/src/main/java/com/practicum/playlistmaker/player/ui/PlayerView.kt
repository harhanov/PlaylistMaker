package com.practicum.playlistmaker.player.ui



interface PlayerView {
    fun updateTimer(timerValue: String)
    fun setPlayButtonIcon(isPlaying: Boolean)
    fun onCompletePlaying()
    fun startPostDelay()
}