package com.practicum.playlistmaker.media_library.playlists.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val interactor: PlaylistInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<NewPlaylistScreenState>()
    val screenState: LiveData<NewPlaylistScreenState> = _screenState
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    private val _playlistInfo = MutableLiveData<PlaylistModel>()
    val playlistInfo: LiveData<PlaylistModel> = _playlistInfo

    private val _onBackPressed = MutableLiveData<Unit>()
    val onBackPressed: LiveData<Unit> get() = _onBackPressed

    private val _isEditMode = MutableLiveData<Boolean>()
    val isEditMode: LiveData<Boolean> get() = _isEditMode
    fun setPlaylistInfo(playlistId: Long) {
        viewModelScope.launch {
            try {
                val playlistInfo = interactor.getPlaylistById(playlistId)
                _playlistInfo.postValue(playlistInfo)
                _isEditMode.postValue(true) // Устанавливаем режим редактирования
            } catch (_: Exception) {
                _isEditMode.postValue(false) // Если произошла ошибка, режим редактирования не устанавливаем
            }
        }
    }

    fun updatePlaylist(playlistModel: PlaylistModel) {
        viewModelScope.launch {
            try {
                // Получаем текущий плейлист, чтобы сохранить URI изображения
                val currentPlaylist = interactor.getPlaylistById(playlistModel.playlistId ?: 0)

                // Сохраняем существующий URI изображения, если доступен, в противном случае используем новый
                val updatedImagePath = playlistModel.playlistImagePath
                    ?: currentPlaylist.playlistImagePath
                val updatedPlaylist = playlistModel.copy(playlistImagePath = updatedImagePath)
                val isPlaylistUpdated = interactor.updatePlaylist(updatedPlaylist)
                if (isPlaylistUpdated) {
//                    _toastMessage.postValue("Изменения сохранены")
                    _onBackPressed.postValue(Unit)
                } else {
                    _toastMessage.postValue("Ошибка при сохранении изменений")
                }
            } catch (e: Exception) {
                _toastMessage.postValue("Ошибка при сохранении изменений")
            }
        }
    }

    fun updateCreateButtonState(text: String) {
        _screenState.value = NewPlaylistScreenState.CreateButtonActiveState(text.isNotEmpty())
    }

    fun onNewPlaylistCreateClick(playlistModel: PlaylistModel) {
        viewModelScope.launch {
            val isPlaylistAdded = interactor.addPlaylist(playlistModel)
            if (isPlaylistAdded) {
                _toastMessage.postValue("Плейлист с названием ${playlistModel.playlistName.toString()} создан")
            } else {
                _toastMessage.postValue("Ошибка при создании плейлиста")
            }
        }
    }

}