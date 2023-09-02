package com.practicum.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.data.model.Track


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel : SearchViewModel
    private lateinit var historyListAdapter: TrackListAdapter
    private lateinit var trackListAdapter: TrackListAdapter
    private var searchTextValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding.searchBackButton.apply {
            setOnClickListener { finish() }
        }
        viewModel.screenStateLD.observe(this) { screenStateLD ->
            if (viewModel.shouldDisplayScreenState(screenStateLD, binding.searchEditText.text.toString())) {
                when (screenStateLD) {
                    is SearchScreenState.Success -> {
                        screenStateLD.tracks?.let { trackListAdapter.setTracks(it) }
                    }
                    is SearchScreenState.ShowHistory -> {
                        screenStateLD.tracks?.let { historyListAdapter.setTracks(it) }
                    }
                    else -> {}
                }
                screenStateLD.render(binding)
            }
        }
        initRecycler()
        initEditText(savedInstanceState)
        handleButtons()
    }

    override fun onResume() {
        super.onResume()
        if (binding.searchEditText.text.toString().isNotEmpty()){
            viewModel.trackSearch(binding.searchEditText.text.toString())
        }else{
            viewModel.showHistory()
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT, binding.searchEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchTextValue = savedInstanceState.getString(INPUT_TEXT) ?: ""
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun handleButtons() {
        binding.inputLineClearButton.apply {
            setOnClickListener {
                binding.searchEditText.text.clear()
                binding.searchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
                viewModel.showHistory()
            }
        }
        binding.clearHistoryButton.apply {
            setOnClickListener {
                viewModel.clearSearchHistory()
            }
        }
        binding.searchRefreshButton.apply {
            setOnClickListener {
                if (binding.searchEditText.text.toString().isNotEmpty()) {
                    viewModel.trackSearch()
                }
            }
        }
    }

    private fun initRecycler() {
        historyListAdapter = TrackListAdapter {
            if (viewModel.clickDebounce()) {
                viewModel.addToHistory(it)
                navigateTo(PlayerActivity::class.java, it)
            }
        }
        binding.rvHistoryList.apply {
            adapter = historyListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        trackListAdapter = TrackListAdapter {
            if (viewModel.clickDebounce()) {
                viewModel.addToHistory(it)
                navigateTo(PlayerActivity::class.java, it)
            }
        }
        binding.rvSongsList.apply {
            adapter = trackListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }


    private fun initEditText(savedInstanceState: Bundle?) {
        binding.searchEditText
            .apply {
                restoreTextFromBundle(textField = this, savedInstanceState = savedInstanceState)
                setOnEditorActionListener { _, actionId, _ ->
                    onClickOnEnterOnVirtualKeyboard(actionId)
                }
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus && binding.searchEditText.text.isEmpty()) viewModel.showHistory()
                }
                doOnTextChanged { text, _, _, _ ->
                    if (binding.searchEditText.hasFocus() && text.toString().isEmpty()) {
                        viewModel.showHistory()
                    }
                    viewModel.searchDebounce(binding.searchEditText.text.toString())
                    binding.inputLineClearButton.visibility = clearButtonVisibility(text.toString())
                }
            }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun restoreTextFromBundle(textField: EditText, savedInstanceState: Bundle?){
        if (savedInstanceState != null) {
            if (savedInstanceState.getString(INPUT_TEXT)!!.isNotEmpty()) {
                textField.setText(savedInstanceState.getString(INPUT_TEXT)!!)
                viewModel.trackSearch(savedInstanceState.getString(INPUT_TEXT)!!)
            }
        }
    }
    private fun onClickOnEnterOnVirtualKeyboard(actionId: Int): Boolean{
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (binding.searchEditText.text.toString().isNotEmpty()) {
                viewModel.trackSearch(binding.searchEditText.text.toString())
            }
        }
        return false
    }

    private fun navigateTo(clazz: Class<PlayerActivity>, track: Track) {
        val intent = Intent(this, clazz)
        intent.putExtra(TRACK, track)
        startActivity(intent)
    }

    companion object{
        private const val INPUT_TEXT = "searchTextValue"
        private const val TRACK = "track"
    }
}