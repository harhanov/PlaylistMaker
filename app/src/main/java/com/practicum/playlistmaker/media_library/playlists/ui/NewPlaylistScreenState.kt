package com.practicum.playlistmaker.media_library.playlists.ui

import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding

sealed class NewPlaylistScreenState() {

    class CreateButtonActiveState(private val isNameFilled: Boolean) : NewPlaylistScreenState() {
        override fun render(binding: FragmentNewPlaylistBinding) {
            binding.playlistCreateButton.isEnabled = isNameFilled
        }
    }
    abstract fun render(binding: FragmentNewPlaylistBinding)
}

