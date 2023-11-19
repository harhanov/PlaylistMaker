package com.practicum.playlistmaker.media_library.playlists.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val interactor: PlaylistInteractor,
) : ViewModel() {

    private val _screenState = MutableLiveData<NewPlaylistScreenState>()
    val screenState: LiveData<NewPlaylistScreenState> = _screenState
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun updateCreateButtonState(text: String) {
        _screenState.value = NewPlaylistScreenState.CreateButtonActiveState(!text.isNullOrEmpty())
    }

    fun onNewPlaylistCreateClick(playlistModel: PlaylistModel) {
        viewModelScope.launch {
            val isPlaylistAdded = interactor.addPlaylist(playlistModel)
            if (isPlaylistAdded) {
                _toastMessage.postValue("Плейлист с названием ${playlistModel.playlistName.toString()} создан")
                Log.d("MyTag", "Playlist Image Path: ${playlistModel.playlistName}")
            } else {
                _toastMessage.postValue("Ошибка при создании плейлиста")
            }
        }
    }

}