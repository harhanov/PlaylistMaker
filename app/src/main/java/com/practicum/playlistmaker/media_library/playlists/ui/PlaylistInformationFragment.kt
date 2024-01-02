package com.practicum.playlistmaker.media_library.playlists.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistInformationBinding
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel
import com.practicum.playlistmaker.player.domain.TrackModel
import com.practicum.playlistmaker.search.data.model.Track
import com.practicum.playlistmaker.search.ui.TrackListAdapter
import com.practicum.playlistmaker.utils.BottomNavigationUtils
import com.practicum.playlistmaker.utils.PlaylistUtils
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistInformationFragment : Fragment(R.layout.playlist_information) {
    private var _binding: PlaylistInformationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistInformationViewModel by viewModel {
        parametersOf(parseIntent())
    }
    private val trackListAdapter: TrackListAdapter by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = PlaylistInformationBinding.inflate(inflater, container, false)
        BottomNavigationUtils.hideBottomNavigationView(activity)
        binding.playlistBackButton.apply {
            setOnClickListener { findNavController().navigateUp() }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.totalPlayingTime.observe(viewLifecycleOwner) { totalPlayingTime ->
            totalPlayingTime?.let {
                binding.totalPlayingTime.text = it
            }
        }

        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            playlist?.let {
                if (viewModel.totalPlayingTime.value == null) {
                    viewModel.calculateTotalPlayingTime()
                }
                updateUI(it)
            }
        }
        viewModel.playlistTracks.observe(viewLifecycleOwner) { tracks ->
            tracks?.let {
                trackListAdapter.setTracks(it.map(TrackModel::trackModelToTrack))
            }
        }
        initRecycler()
    }

    private fun initRecycler() {

        trackListAdapter.onClickListener = { track ->
            navigateToPlayerFragment(track)
            BottomNavigationUtils.hideBottomNavigationView(activity)
        }

        binding.trackRecyclerView.apply {
            adapter = trackListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    private fun navigateToPlayerFragment(track: Track) {
        val action = PlaylistInformationFragmentDirections.actionPlaylistInformationFragmentToPlayerFragment(track)
        findNavController().navigate(action)
    }

    private fun updateUI(playlist: PlaylistModel) {
        binding.playlistTitle.text = playlist.playlistName
        binding.playlistDescription.text = playlist.playlistDescription
        binding.numberOfTracks.text =
            PlaylistUtils.formatNumberOfTracks(playlist.numberOfTracks, binding.root.resources)
        binding.totalPlayingTime.text = viewModel.totalPlayingTime.value
        Glide.with(binding.playlistCover)
            .load(playlist.playlistImagePath)
            .placeholder(R.drawable.playlist_placeholder)
            .into(binding.playlistCover)
        val tracksList = viewModel.playlistTracks.value
        val tracks = viewModel.playlistTracks.value?.map { it.trackModelToTrack() } ?: emptyList()
        trackListAdapter.setTracks(tracks)
    }

    private fun parseIntent(): Long {
        return requireArguments().getLong(PLAYLIST_ID_KEY, 0L)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val PLAYLIST_ID_KEY = "playlistId"
    }
}