package com.practicum.playlistmaker.media_library.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MediaLibraryViewPagerAdapter(
    parentFragment: Fragment,
    private val tracks: String,
    private val playlists: String
) : FragmentStateAdapter(parentFragment) {
    override fun getItemCount() = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavouritesTracksFragment.newInstance(tracks)
            else -> PlaylistsFragment.newInstance(playlists)
        }
    }
}