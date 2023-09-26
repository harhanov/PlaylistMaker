package com.practicum.playlistmaker.media_library.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaLibraryBinding

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val favTracks = intent.getStringExtra("favorite_tracks_information") ?: ""
        val playlists = intent.getStringExtra("playlists_information") ?: ""

        binding.mlViewPager.adapter = MediaLibraryViewPagerAdapter(supportFragmentManager,
            lifecycle, favTracks, playlists)

        binding.mlSearchBackButton.setOnClickListener{
            finish()
        }

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.mlViewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}