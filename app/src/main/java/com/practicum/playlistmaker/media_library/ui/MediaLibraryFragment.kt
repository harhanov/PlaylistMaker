package com.practicum.playlistmaker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaLibraryBinding

class MediaLibraryFragment : Fragment() {
    private var _binding: FragmentMediaLibraryBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favTracks = arguments?.getString(FAVOURITES_TRACKS_INFORMATION).orEmpty()
        val playlists = arguments?.getString(PLAYLIST_INFORMATION).orEmpty()

        binding.mlViewPager.adapter = MediaLibraryViewPagerAdapter(
            this,
            favTracks,
            playlists
        )

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.mlViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tabMediator.detach()
    }

    companion object {

        private const val FAVOURITES_TRACKS_INFORMATION = "favorite_tracks_information"
        private const val PLAYLIST_INFORMATION = "playlists_information"
    }

}