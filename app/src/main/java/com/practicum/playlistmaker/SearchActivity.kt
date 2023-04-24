package com.practicum.playlistmaker


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity(), TrackListAdapter.OnTrackClickListener {
    private lateinit var errorText: TextView
    private lateinit var errorPicture: ImageView
    private lateinit var backFromSearch: View
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var rvTrackList: RecyclerView
    private lateinit var refreshButton: Button
    private lateinit var errorMessage: LinearLayout

    private lateinit var rvHistoryList: RecyclerView
    private lateinit var clearHistoryButton: Button
    private lateinit var historyList: LinearLayout
    private lateinit var searchHistoryManager: SearchHistoryManager

    private var searchTextValue = ""
    private val trackListAdapter = TrackListAdapter()
    private val historyListAdapter = TrackListAdapter()
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(iTunesAPIService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        errorText = findViewById(R.id.error_text)
        errorPicture = findViewById(R.id.error_wh)
        backFromSearch = findViewById(R.id.searchBackButton)
        inputEditText = findViewById(R.id.searchEditText)
        clearButton = findViewById(R.id.clearIcon)
        rvTrackList = findViewById(R.id.rvSongsList)
        refreshButton = findViewById(R.id.search_refresh_button)
        errorMessage = findViewById(R.id.various_errors)

        rvHistoryList = findViewById(R.id.rvHistoryList)
        clearHistoryButton = findViewById(R.id.clear_history_button)
        historyList = findViewById(R.id.search_history_list)
        searchHistoryManager = SearchHistoryManager(this)

        trackListAdapter.onTrackClickListener = this

        if (inputEditText.hasFocus()) updateTrackList()

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) updateTrackList()
        }

        clearButton.setOnClickListener {
            clearSearch()
        }

        clearHistoryButton.setOnClickListener {
            clearSearchHistory()
        }

        backFromSearch.setOnClickListener {
            finish()
        }

        refreshButton.setOnClickListener {
            trackSearch()
            errorMessage.visibility = View.GONE
        }

        val simpleTextWatcher = object : SimpleTextWatcher {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchTextValue = s.toString()
                historyList.isVisible =
                    (inputEditText.hasFocus() && (s?.isNotEmpty() != true) && searchHistoryManager.getSearchHistory()
                        .isNotEmpty())
            }
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)

        rvTrackList.adapter = trackListAdapter
        rvTrackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvHistoryList.adapter = historyListAdapter
        rvHistoryList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trackSearch()
                hideKeyboard()
                true
            } else false
        }

        historyListAdapter.onTrackClickListener = this
    }

    private fun trackSearch() {
        val searchText = inputEditText.text.toString()
        iTunesService.search(searchText).enqueue(object : Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                if (response.isSuccessful) {
                    if (response.body()?.results?.isNotEmpty() == true) {
                        trackListAdapter.setTracks(response.body()?.results!!)
                        errorMessage.isVisible = false
                        refreshButton.isVisible = false

                    } else {
                        showNetworkErrorView(false)
                    }
                } else {
                    showNetworkErrorView(true)
                }
            }

            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                showNetworkErrorView(true)
                Log.e("API_ERROR", t.message ?: "Unknown error")
                showNetworkErrorView(true)
            }
        })
    }

    private fun showNetworkErrorView(isNetworkError: Boolean) {
        errorMessage.isVisible = true
        refreshButton.isVisible = isNetworkError
        trackListAdapter.setTracks(null)
        if (isNetworkError) {
            errorPicture.setImageResource(R.drawable.no_connection)
            errorText.text = getString(R.string.no_internet_error)
        } else {
            errorPicture.setImageResource(R.drawable.bad_request)
            errorText.text = getString(R.string.nothing_found)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchTextValue", searchTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchTextValue = savedInstanceState.getString("searchTextValue") ?: ""
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }


    private fun clearSearch() {
        inputEditText.setText("")
        trackListAdapter.setTracks(emptyList())
        errorMessage.isVisible = false
        hideKeyboard()
    }

    private fun clearSearchHistory() {
        searchHistoryManager.clearHistory()
        historyList.isVisible = false
    }

    override fun onTrackClick(track: Track) {
        searchHistoryManager.addToHistory(track)
        historyListAdapter.setTracks(searchHistoryManager.getSearchHistory())
        val intent = Intent(this, PlayerActivity::class.java).apply {
            putExtra("trackName", track.trackName)
            putExtra("artistName", track.artistName)
            putExtra("trackTime", track.trackTime)
            putExtra("artworkUrl100", track.artworkUrl100)
            putExtra("collectionName", track.collectionName)
            putExtra("releaseDate", track.releaseDate)
            putExtra("primaryGenreName", track.primaryGenreName)
            putExtra("country", track.country)
        }
        startActivity(intent)
    }

    private fun updateTrackList() {
        val searchHistory = searchHistoryManager.getSearchHistory()
        if (searchHistory.isNotEmpty() && inputEditText.text.isEmpty()) {
            historyListAdapter.setTracks(searchHistory)
            historyList.isVisible = true
        } else {
            historyList.isVisible = false
        }
    }

}