package com.practicum.playlistmaker.player.ui

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.domain.TrackModel
import com.practicum.playlistmaker.search.data.model.Track
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(parseIntent())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playerBackButton.apply {
            setOnClickListener { finish() }
        }
        binding.playPauseButton.apply {
            setOnClickListener {
                viewModel.playbackControl()
            }
        }
        binding.favoriteButton.apply {
            setOnClickListener{
                viewModel.viewModelScope.launch {
                    viewModel.onFavoriteClicked()
                }
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

    private fun parseIntent(): TrackModel {
        if (!intent.hasExtra(TRACK_KEY)) {
            throw RuntimeException("Track is absent")
        }
        return try {
            val track = intent.getParcelableExtra<Track?>(TRACK_KEY) as Track
            track.mapTrackToTrackForPlayer()
        } catch (e: Exception) {
            throw RuntimeException("Unknown param format in extra")
        }
    }

    companion object {
        private const val TRACK_KEY = "track"
    }
}