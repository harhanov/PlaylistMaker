package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.*
import com.practicum.playlistmaker.search.data.network.SUCCESS_CODE
import com.practicum.playlistmaker.search.data.model.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class SearchViewModel : ViewModel(), KoinComponent {
    private val tracksInteractor: TracksInteractor by inject()
    private val screenState = MutableLiveData<SearchScreenState>()
    val screenStateLD: LiveData<SearchScreenState> = screenState
    private val _isClickAllowed = MutableLiveData<Boolean>()
    val isClickAllowed: LiveData<Boolean> = _isClickAllowed
    private val _isSearchTextChanged = MutableLiveData<Boolean>()
    private val isSearchTextChanged: LiveData<Boolean> = _isSearchTextChanged
    private var lastQuery: String? = null
    private var searchJob: Job? = null

    fun trackSearch(searchText: String? = lastQuery) {
        val searchTextChanged = isSearchTextChanged.value ?: false
        if (!searchTextChanged) {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
                searchText.let { query ->
                    updateScreenState(SearchScreenState.Loading())
                    if (query != null) {
                        tracksInteractor
                            .searchTracks(query)
                            .collect { triple ->
                                processResult(triple.first, triple.second, triple.third)
                            }
                    }
                }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?, code: Int) {
        val tracks = mutableListOf<Track>()
        if (!foundTracks.isNullOrEmpty()) {
            tracks.addAll(foundTracks)
        }
        when (code) {
            SUCCESS_CODE -> {
                if (!foundTracks.isNullOrEmpty()) {
                    updateScreenState(
                        SearchScreenState.Success(
                            tracks = foundTracks
                        )
                    )
                } else {
                    updateScreenState(SearchScreenState.NothingFound())
                }
            }

            else -> {
                updateScreenState(SearchScreenState.Error(errorMessage))
            }
        }

    }

    fun setSearchTextNotChanged(isTextChanged: Boolean) {
        _isSearchTextChanged.postValue(isTextChanged)
    }

    private fun updateScreenState(newState: SearchScreenState) {
        screenState.postValue(newState)
    }

    fun setClickAllowed(allowed: Boolean) {
        _isClickAllowed.value = allowed
        if (allowed) {
            viewModelScope.launch(Dispatchers.Main) {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                _isClickAllowed.value = true
            }
        }
    }


    fun searchDebounce(newQuery: String? = lastQuery) {
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
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}

