package com.practicum.playlistmaker.search.ui

import androidx.core.view.isVisible
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.Track

sealed class SearchScreenState(val message: String? = null, val tracks: List<Track>? = null) {

    class Loading : SearchScreenState() {
        override fun render(binding: ActivitySearchBinding) {
            binding.progressBar.isVisible = true
            binding.placeholderMessage.isVisible = false
            binding.clearHistoryButton.isVisible = false
            binding.searchRefreshButton.isVisible = false
        }
    }

    class NothingFound : SearchScreenState() {
        override fun render(binding: ActivitySearchBinding) {
            binding.progressBar.isVisible = false
            binding.placeholderMessage.isVisible = true
            binding.clearHistoryButton.isVisible = false
            binding.errorPh.setImageResource(R.drawable.bad_request)
            binding.placeholderMessage.text =
                binding.placeholderMessage.resources.getText(R.string.nothing_found)
        }
    }

    class Error(message: String?) : SearchScreenState(message = message) {
        override fun render(binding: ActivitySearchBinding) {
            binding.progressBar.isVisible = false
            binding.placeholderMessage.isVisible = true
            binding.clearHistoryButton.isVisible = false
            binding.searchRefreshButton.isVisible = true
            binding.errorPh.setImageResource(R.drawable.no_connection)
            binding.placeholderMessage.text = message
        }
    }

    class Success(tracks: List<Track>?) : SearchScreenState(tracks = tracks) {
        override fun render(binding: ActivitySearchBinding) {
            binding.progressBar.isVisible = false
            binding.placeholderMessage.isVisible = false
            binding.clearHistoryButton.isVisible = false
            binding.searchRefreshButton.isVisible = false
        }
    }

    class ShowHistory(tracks: List<Track>?) : SearchScreenState(tracks = tracks) {
        override fun render(binding: ActivitySearchBinding) {
            if (tracks.isNullOrEmpty()) {
                binding.clearHistoryButton.isVisible = false
                binding.placeholderMessage.isVisible = false
            } else {
                binding.clearHistoryButton.isVisible = true
                binding.placeholderMessage.isVisible = true
            }
            binding.progressBar.isVisible = false
            binding.searchRefreshButton.isVisible = false
        }
    }

    abstract fun render(binding: ActivitySearchBinding)
}
