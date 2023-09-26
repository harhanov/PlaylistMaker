package com.practicum.playlistmaker.media_library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavouritesTracksViewModel(private val favTracks: String) : ViewModel() {

    private val tracksLiveData = MutableLiveData(favTracks)
    fun observeUrl(): LiveData<String> = tracksLiveData
}