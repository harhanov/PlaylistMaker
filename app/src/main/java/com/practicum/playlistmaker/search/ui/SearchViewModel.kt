package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.*
import com.practicum.playlistmaker.search.data.network.SUCCESS_CODE
import com.practicum.playlistmaker.search.data.model.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class SearchViewModel(application: Application) : AndroidViewModel(application), KoinComponent {
    private val tracksInteractor: TracksInteractor by inject()
    private val screenState = MutableLiveData<SearchScreenState>()
    val screenStateLD: LiveData<SearchScreenState> = screenState
    private val _isClickAllowed = MutableLiveData<Boolean>()
    val isClickAllowed: LiveData<Boolean> = _isClickAllowed
    private val handler = Handler(Looper.getMainLooper())
    private var lastQuery: String? = null


    fun trackSearch(searchText: String? = lastQuery) {
        val searchRunnable = Runnable {
            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
            searchText.let { query ->
                updateScreenState(SearchScreenState.Loading())
                if (query != null) {
                    tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {
                        override fun consume(
                            foundTracks: List<Track>?,
                            errorMessage: String?,
                            code: Int
                        ) {
                            when (code) {
                                SUCCESS_CODE -> {
                                    if (!foundTracks.isNullOrEmpty()) {
                                        updateScreenState(SearchScreenState.Success(foundTracks))
                                    } else {
                                        updateScreenState(SearchScreenState.NothingFound())
                                    }
                                }
                                else -> {
                                    updateScreenState(SearchScreenState.Error(errorMessage))
                                }
                            }
                        }
                    })
                }
            }
        }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime
        )
    }

    private fun updateScreenState(newState: SearchScreenState) {
        screenState.postValue(newState)
    }

    fun setClickAllowed(allowed: Boolean) {
        _isClickAllowed.value = allowed
        if (allowed) {
            handler.postDelayed({ _isClickAllowed.value = true }, CLICK_DEBOUNCE_DELAY)
        }
    }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(newQuery: String? = lastQuery) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        if (!newQuery.isNullOrEmpty()) {
            if ((lastQuery == newQuery)) {
                return
            }
            this.lastQuery = newQuery
            trackSearch(newQuery)
        }
    }

    fun addToHistory(track: Track) {
        tracksInteractor.addTrackToHistory(track)
    }

    fun clearSearchHistory() {
        tracksInteractor.clearHistory()
        screenState.postValue(SearchScreenState.ShowHistory(null))
    }

    fun showHistory() {
        if (!tracksInteractor.getHistory().isNullOrEmpty()) {
            screenState.value = SearchScreenState.ShowHistory(tracksInteractor.getHistory())
        } else {
            screenState.value = SearchScreenState.Success(null)
        }
    }

    fun shouldDisplayScreenState(screenStateLD: SearchScreenState, queryText: String): Boolean {
        if ((screenStateLD is SearchScreenState.Success
                    && queryText.isNotEmpty())
            || (screenStateLD !is SearchScreenState.Success)
        ) {
            return true
        }
        return false
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}

