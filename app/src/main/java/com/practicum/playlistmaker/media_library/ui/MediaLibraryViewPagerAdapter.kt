package com.practicum.playlistmaker.media_library.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MediaLibraryViewPagerAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle,
    private val tracks: String, private val playlists: String
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> FavTracksFragment.newInstance(tracks)
                else -> PlaylistsFragment.newInstance(playlists)
            }
        }
}