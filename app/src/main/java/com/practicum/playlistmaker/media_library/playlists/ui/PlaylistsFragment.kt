package com.practicum.playlistmaker.media_library.playlists.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val playlistsViewModel: PlaylistsViewModel by viewModel()
    private lateinit var binding: FragmentPlaylistsBinding
    private val playlistAdapter: PlaylistAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun hideBottomNavigationView() {
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.GONE
    }

    private fun showBottomNavigationView() {
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newPlaylistButton.setOnClickListener {
            hideBottomNavigationView()
            openNewPlaylist()
        }

        binding.playlistsRecyclerView.adapter = playlistAdapter
        binding.playlistsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        setupPlaylistsObserver()

    }

    private fun openNewPlaylist() {
        val navController = findNavController()
        navController.navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)
    }


    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

    private fun setupPlaylistsObserver() {
        playlistsViewModel.playlistsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsState.PlaylistsLoaded -> {
                    val playlists = state.playlists
                    Log.d("PlaylistsView", "Playlists loaded: ${playlists.size} playlists")
                    playlistAdapter.setPlaylistList(playlists)
                    binding.playlistsRecyclerView.visibility = View.VISIBLE
                    binding.playlistErrorPh.visibility = View.GONE
                    playlistAdapter.notifyDataSetChanged()
                }

                is PlaylistsState.Empty -> {
                    Log.d("PlaylistsView", "No playlists loaded")
                    playlistAdapter.setPlaylistList(emptyList())
                    binding.playlistsRecyclerView.visibility = View.GONE
                    binding.playlistErrorPh.visibility = View.VISIBLE
                    playlistAdapter.notifyDataSetChanged()
                }
            }
        }
    }

}