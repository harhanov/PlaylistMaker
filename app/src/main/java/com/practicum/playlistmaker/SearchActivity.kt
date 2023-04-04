package com.practicum.playlistmaker


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private lateinit var errorText: TextView
    private lateinit var errorViewHolder: ImageView
    private lateinit var backFromSearch: View
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var rvTrackList: RecyclerView
    private lateinit var refreshButton: Button
    private lateinit var errorMessage: LinearLayout

    private var searchTextValue = ""
    private val trackListAdapter = TrackListAdapter()
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
        errorViewHolder = findViewById(R.id.error_wh)
        backFromSearch = findViewById(R.id.searchBackButton)
        inputEditText = findViewById(R.id.searchEditText)
        clearButton = findViewById(R.id.clearIcon)
        rvTrackList = findViewById(R.id.rvSongsList)
        refreshButton = findViewById(R.id.search_refresh_button)
        errorMessage = findViewById(R.id.various_errors)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            trackListAdapter.setTracks(emptyList())
            errorMessage.visibility = View.GONE
            hideKeyboard()
        }

        backFromSearch.setOnClickListener {
            finish()
        }

        refreshButton.setOnClickListener {
            trackSearch()
            errorMessage.visibility = View.GONE
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchTextValue = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setText(searchTextValue)

        rvTrackList.adapter = trackListAdapter
        rvTrackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trackSearch()
                hideKeyboard()
                true
            } else false
        }
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
            }
        })
    }

    private fun showNetworkErrorView(isNetworkError: Boolean) {
        errorMessage.isVisible = true
        refreshButton.isVisible = isNetworkError
        trackListAdapter.setTracks(null)
        if (isNetworkError) {
            errorViewHolder.setImageResource(R.drawable.no_connection)
            errorText.text = getString(R.string.no_internet_error)
        } else {
            errorViewHolder.setImageResource(R.drawable.bad_request)
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
}