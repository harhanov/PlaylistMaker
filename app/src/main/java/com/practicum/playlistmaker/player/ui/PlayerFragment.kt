package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel
import com.practicum.playlistmaker.media_library.playlists.ui.PlaylistAdapter
import com.practicum.playlistmaker.media_library.playlists.ui.PlaylistsState
import com.practicum.playlistmaker.player.domain.TrackModel
import com.practicum.playlistmaker.search.data.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment(R.layout.fragment_player) {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private var shouldUpdatePlaylists = false
    private val playlistAdapter: PlaylistAdapter by inject {
        parametersOf(
            requireContext(),
            R.layout.playlist_item_mini,
            R.dimen.mini_cover_corner_radius
        )
    }

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(parseIntent())
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentPlayerBinding.inflate(inflater, container, false)

        binding.playerBackButton.apply {
            setOnClickListener { findNavController().navigateUp() }
        }

        binding.playPauseButton.apply {
            setOnClickListener {
                viewModel.playbackControl()
            }
        }

        binding.favoriteButton.apply {
            setOnClickListener {
                viewModel.viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        viewModel.onFavoriteClicked()
                    }
                }
            }
        }

        val bottomSheetContainer = binding.bottomSheetMenu

        val overlay = binding.overlay

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.playlistsRecyclerView.adapter = playlistAdapter
        binding.playlistsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.addToPlaylistButton.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.toggleBottomSheetVisibility()
                viewModel.updatePlaylists()
                playlistAdapter.setClickerListener(object : PlaylistAdapter.Clicker {
                    override fun onClick(playlist: PlaylistModel) {
                        viewModel.onPlaylistItemClick(playlist)
                    }
                })

                setupPlaylistsObserver()
            }

        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        viewModel.screenState.observe(viewLifecycleOwner) {
            it.render(binding)
        }

        binding.newPlaylistButtonMini.setOnClickListener {
            openNewPlaylist()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bottomSheetLiveData.observe(viewLifecycleOwner) { state ->
            bottomSheetBehavior.state = state
        }




        viewModel.playerEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is PlayerScreenState.PlayerEvent.NavigateBackToPlayerFragment -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    shouldUpdatePlaylists = true
                }
            }
        }
    }

    private fun setupPlaylistsObserver() {
        viewModel.playlistsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsState.PlaylistsLoaded -> {
                    val playlists = state.playlists
                    playlistAdapter.setPlaylistsList(playlists)
                    playlistAdapter.notifyItemInserted(playlists.size - 1)
                }

                is PlaylistsState.Empty -> {
                    binding.playlistsRecyclerView.visibility = View.GONE
                }
            }
        }
    }


    private fun openNewPlaylist() {
        viewModel.setBottomSheetState(bottomSheetBehavior.state)
        val action = PlayerFragmentDirections.actionPlayerFragmentToNewPlaylistFragment()
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    //
    override fun onResume() {
        super.onResume()
        setupPlaylistsObserver()
        viewModel.preparePlayer()
    }

    @Suppress("DEPRECATION")
    private fun parseIntent(): TrackModel {
        if (requireArguments().containsKey(TRACK_KEY)) {
            return try {
                val track = requireArguments().getParcelable<Track?>(TRACK_KEY)
                    ?: throw IllegalArgumentException("Track is absent or has an invalid format")
                track.mapTrackToTrackForPlayer()
            } catch (e: Exception) {
                throw RuntimeException("Error parsing track from intent", e)
            }
        } else {
            throw IllegalArgumentException("Track is absent in arguments")
        }
    }

    companion object {
        const val TRACK_KEY = "track"
    }
}
