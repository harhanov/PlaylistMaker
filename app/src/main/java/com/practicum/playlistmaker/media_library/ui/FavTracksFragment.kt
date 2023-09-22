package com.practicum.playlistmaker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFavoritesTracksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavTracksFragment : Fragment() {

    companion object {
        private const val FAV_TRACKS = "favorite_tracks"

        fun newInstance(favoritesTracks: String) = FavTracksFragment().apply {
            arguments = Bundle().apply {
                putString(FAV_TRACKS, favoritesTracks)
            }
        }
    }

    private val favTracksViewModel: FavTracksViewModel by viewModel {
        parametersOf(requireArguments().getString(FAV_TRACKS))
    }

    private lateinit var binding: FragmentFavoritesTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favTracksViewModel.observeUrl().observe(viewLifecycleOwner) {
        }
    }
}