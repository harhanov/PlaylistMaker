package com.practicum.playlistmaker.player.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlayerViewModel(
    private val trackId: Int,
) : ViewModel(){

    init {
        Log.d("TEST", "init!")
    }

    companion object {
        fun getViewModelFactory(trackId: Int): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                // 1
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        trackId
                    ) as T
                }
            }

}
}