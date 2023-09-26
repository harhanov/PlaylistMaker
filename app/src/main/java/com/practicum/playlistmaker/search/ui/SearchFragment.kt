package com.practicum.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.data.model.Track
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: SearchViewModel by viewModel()
    private val historyListAdapter: TrackListAdapter by inject()
    private val trackListAdapter: TrackListAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        viewModel.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.screenStateLD.observe(viewLifecycleOwner) { screenStateLD ->
            if (viewModel.shouldDisplayScreenState(
                    screenStateLD,
                    binding.searchEditText.text.toString()
                ) &&
                viewModel.isClickAllowed.value == true
            ) {

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
        viewModel.setClickAllowed(true)
        if (binding.searchEditText.text.toString().isNotEmpty()) {
            viewModel.trackSearch(binding.searchEditText.text.toString())
        } else {
            viewModel.showHistory()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.onDestroy()
    }

    private fun handleButtons() {
        binding.inputLineClearButton.apply {
            setOnClickListener {
                binding.searchEditText.text.clear()
                binding.searchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
                trackListAdapter.setTracks(emptyList())
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

        trackListAdapter.onClickListener = { track ->
            if (viewModel.isClickAllowed.value == true) {
                viewModel.addToHistory(track)
                viewModel.setClickAllowed(false)
                navigateTo(PlayerActivity::class.java, track)
            }
        }

        historyListAdapter.onClickListener = { track ->
            if (viewModel.isClickAllowed.value == true) {
                viewModel.addToHistory(track)
                viewModel.setClickAllowed(false)
                navigateTo(PlayerActivity::class.java, track)
            }
        }

        binding.rvHistoryList.apply {
            adapter = historyListAdapter
            layoutManager = LinearLayoutManager(this.context)
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

    private fun restoreTextFromBundle(textField: EditText, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val savedText = savedInstanceState.getString(INPUT_TEXT)
            if (!savedText.isNullOrEmpty()) {
                textField.setText(savedText)
                viewModel.trackSearch(savedText)
            }
        }
    }

    private fun onClickOnEnterOnVirtualKeyboard(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (binding.searchEditText.text.toString().isNotEmpty()) {
                viewModel.trackSearch(binding.searchEditText.text.toString())
            }
        }
        return false
    }

    private fun navigateTo(clazz: Class<PlayerActivity>, track: Track) {
        val intent = Intent(requireContext(), clazz)
        intent.putExtra(TRACK, track)
        startActivity(intent)
    }

    companion object {
        private const val INPUT_TEXT = "searchTextValue"
        private const val TRACK = "track"
    }
}