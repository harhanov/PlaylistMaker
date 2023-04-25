package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistoryManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)

    private val gson: Gson = Gson()

    fun addToHistory(track: Track) {
        val trackId = track.trackId

        val searchHistoryString = sharedPreferences.getString("search_history", null)
        val searchHistory = if (searchHistoryString != null) {
            gson.fromJson(searchHistoryString, Array<Track>::class.java).toMutableList()
        } else {
            mutableListOf()
        }

        searchHistory.removeAll { it.trackId == trackId }

        searchHistory.add(0, track)

        if (searchHistory.size > 10) {
            searchHistory.removeLast()
        }

        val searchHistoryJson = gson.toJson(searchHistory)
        sharedPreferences.edit().putString("search_history", searchHistoryJson).apply()
    }

    fun clearHistory() {
        sharedPreferences.edit().remove("search_history").apply()
    }

    fun getSearchHistory(): List<Track> {
        val searchHistoryString = sharedPreferences.getString("search_history", null)
        return if (searchHistoryString != null) {
            gson.fromJson(searchHistoryString, Array<Track>::class.java).toList()
        } else {
            emptyList()
        }
    }

}