package com.practicum.playlistmaker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistsFragment : Fragment() {

    companion object {
        private const val PLAYLISTS = "playlists"

        fun newInstance(playlists: String) = PlaylistsFragment().apply {
            arguments = Bundle().apply {
                putString(PLAYLISTS, playlists)
            }
        }
    }

    private val playlistsViewModel: PlaylistsViewModel by viewModel {
        parametersOf(requireArguments().getString(PLAYLISTS))
    }

    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistsViewModel.observeUrl().observe(viewLifecycleOwner) {
        }
    }
}