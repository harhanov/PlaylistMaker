package com.practicum.playlistmaker.player.ui

import android.os.*
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.search.data.model.Track

class PlayerActivity : ComponentActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val track = intent.getParcelableExtra<Track?>("track")
        Log.d("PlayerActivity", "Received intent data: $track")
        if (track != null) {
            viewModel = ViewModelProvider(
                this,
                PlayerViewModel.getViewModelFactory(trackForPlayer = track)
            )[PlayerViewModel::class.java]
        }
        binding.playerBackButton.apply {
            setOnClickListener { finish() }
        }
        binding.playPauseButton.apply {
            setOnClickListener {
                viewModel.playbackControl()
            }
        }
        viewModel.screenState.observe(this) {
            it.render(binding)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }
}