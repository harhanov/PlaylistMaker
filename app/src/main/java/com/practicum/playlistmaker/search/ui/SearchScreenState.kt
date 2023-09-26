package com.practicum.playlistmaker.search.ui

import androidx.core.view.isVisible
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.search.data.model.Track

sealed class SearchScreenState(val message: String? = null, val tracks: List<Track>? = null) {

    class Loading : SearchScreenState() {
        override fun render(binding: FragmentSearchBinding) {
            binding.progressBar.isVisible = true
            binding.variousErrors.isVisible = false
            binding.searchHistoryList.isVisible = false
            binding.searchRefreshButton.isVisible = false
        }
    }

    class NothingFound : SearchScreenState() {
        override fun render(binding: FragmentSearchBinding) {
            binding.progressBar.isVisible = false
            binding.variousErrors.isVisible = true
            binding.searchHistoryList.isVisible = false
            binding.errorPh.setImageResource(R.drawable.bad_request)
            binding.errorText.text =
                binding.placeholderMessage.resources.getText(R.string.nothing_found)
        }
    }

    class Error(message: String?) : SearchScreenState(message = message) {
        override fun render(binding: FragmentSearchBinding) {
            binding.progressBar.isVisible = false
            binding.variousErrors.isVisible = true
            binding.searchHistoryList.isVisible = false
            binding.searchRefreshButton.isVisible = true
            binding.errorPh.setImageResource(R.drawable.no_connection)
            binding.errorText.text = message
        }
    }

    class Success(tracks: List<Track>?) : SearchScreenState(tracks = tracks) {
        override fun render(binding: FragmentSearchBinding) {
            binding.progressBar.isVisible = false
            binding.variousErrors.isVisible = false
            binding.searchRefreshButton.isVisible = false
            binding.searchHistoryList.isVisible = false
        }
    }

    class ShowHistory(tracks: List<Track>?) : SearchScreenState(tracks = tracks) {
        override fun render(binding: FragmentSearchBinding) {
            binding.clearHistoryButton.isVisible = !tracks.isNullOrEmpty()
            binding.searchHistoryList.isVisible = !tracks.isNullOrEmpty()
            binding.progressBar.isVisible = false
            binding.searchRefreshButton.isVisible = false
            binding.variousErrors.isVisible = false
        }
    }

    abstract fun render(binding: FragmentSearchBinding)
}
