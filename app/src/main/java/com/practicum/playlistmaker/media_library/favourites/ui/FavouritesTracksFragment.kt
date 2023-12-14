package com.practicum.playlistmaker.media_library.favourites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.FragmentFavouritesTracksBinding
import com.practicum.playlistmaker.media_library.ui.MediaLibraryFragmentDirections
import com.practicum.playlistmaker.search.ui.TrackListAdapter
import com.practicum.playlistmaker.utils.BottomNavigationUtils
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavouritesTracksFragment : Fragment() {
    private val trackListAdapter: TrackListAdapter by inject()
    private val favouritesTracksViewModel: FavouritesTracksViewModel by viewModel()

    private var _binding: FragmentFavouritesTracksBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavouritesTracksBinding.inflate(inflater, container, false)
        initRecycler()
        return binding.root
    }

    private fun initRecycler() {
        trackListAdapter.onClickListener = { track ->
            findNavController().navigate(
            MediaLibraryFragmentDirections.actionMediaLibraryFragmentToPlayerFragment(track))
            BottomNavigationUtils.hideBottomNavigationView(activity)
        }

        binding.rvFavSongsList.apply {
            adapter = trackListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFavSongsList.adapter = trackListAdapter
        binding.rvFavSongsList.layoutManager = LinearLayoutManager(requireContext())
        setupFavouriteTracksObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        favouritesTracksViewModel.updateFavouritesTrack()
        BottomNavigationUtils.showBottomNavigationView(activity)
    }

    private fun setupFavouriteTracksObserver() {
        favouritesTracksViewModel.favouriteTracksState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavouriteTracksState.TracksLoaded -> {
                    val tracks = state.tracks.map { it.trackModelToTrack() }
                        .sortedByDescending { it.orderAdded }
                    trackListAdapter.setTracks(tracks)
                    binding.rvFavSongsList.visibility = View.VISIBLE
                    binding.favErrorPh.visibility = View.GONE
                    trackListAdapter.notifyDataSetChanged()
                }

                is FavouriteTracksState.Empty -> {
                    trackListAdapter.setTracks(emptyList())
                    binding.rvFavSongsList.visibility = View.GONE
                    binding.favErrorPh.visibility = View.VISIBLE
                    trackListAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}
