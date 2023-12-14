package com.practicum.playlistmaker.search.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.search.data.model.Track
import com.practicum.playlistmaker.utils.BottomNavigationUtils
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
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        viewModel.setSearchTextNotChanged(true)
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

                    else -> Unit
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
        viewModel.setSearchTextNotChanged(false)
        if (binding.searchEditText.text.toString().isNotEmpty()) {
            viewModel.trackSearch(binding.searchEditText.text.toString())
        } else {
            viewModel.showHistory()
        }
        BottomNavigationUtils.showBottomNavigationView(activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                navigateToPlayerFragment(track)
                BottomNavigationUtils.hideBottomNavigationView(activity)
            }
        }

        historyListAdapter.onClickListener = { track ->
            if (viewModel.isClickAllowed.value == true) {
                viewModel.addToHistory(track)
                viewModel.setClickAllowed(false)
                navigateToPlayerFragment(track)
                BottomNavigationUtils.hideBottomNavigationView(activity)
            }
        }

        binding.rvHistoryList.apply {
            adapter = historyListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        binding.rvSongsList.apply {
            if (adapter == null) {
                Log.d("RecyclerView", "Adapter is null before setting")
            }
            adapter = trackListAdapter
            layoutManager = LinearLayoutManager(this.context)
            if (adapter == null) {
                Log.d("RecyclerView", "Adapter is still null after setting")
            } else {
                Log.d("RecyclerView", "Adapter is set successfully")
            }
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

    private fun navigateToPlayerFragment(track: Track) {
        Log.d("Navigation", "Navigating to PlayerActivity with track: $track")
        val action = SearchFragmentDirections.actionSearchFragmentToPlayerFragment(track)
        Log.d("Navigation", "Action created, navigating...")
        findNavController().navigate(action)
        Log.d("Navigation", "Navigation completed")
    }

    companion object {
        private const val INPUT_TEXT = "searchTextValue"
    }
}