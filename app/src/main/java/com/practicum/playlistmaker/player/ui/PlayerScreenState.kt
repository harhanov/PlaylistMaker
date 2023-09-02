package com.practicum.playlistmaker.player.ui

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.search.data.model.Track
import com.practicum.playlistmaker.utils.DateUtils


sealed class PlayerScreenState {
    class BeginningState(val track: Track) : PlayerScreenState() {
        override fun render(binding: ActivityPlayerBinding) {
            binding.songTitle.text = track.trackName
            binding.artistName.text = track.artistName
            binding.tvDurationValue.text = DateUtils.formatTrackTime(track.trackTime)
            binding.tvAlbumValue.text = track.collectionName
            binding.tvReleaseYearValue.text = track.let {
                it.releaseDate?.let { it1 ->
                    DateUtils.extractReleaseYear(
                        it1
                    )
                }
            }
            binding.tvGenreValue.text = track.primaryGenreName
            binding.tvCountryValue.text = track.country

            Glide
                .with(binding.cover)
                .load(track.getCoverArtwork())
                .placeholder(R.drawable.crocozebra)
                .transform(
                    CenterCrop(),
                    RoundedCorners(binding.cover.resources.getDimensionPixelSize(R.dimen.big_cover_corner_radius))
                )
                .into(binding.cover)
        }
    }

    class PlayButtonHandling(private val playerState: PlayerState) : PlayerScreenState() {
        override fun render(binding: ActivityPlayerBinding) {
            when (playerState) {
                PlayerState.PLAYING -> {
                    binding.playPauseButton.setBackgroundResource(R.drawable.pause_button)
                }
                else -> {
                    binding.playPauseButton.setBackgroundResource(R.drawable.ic_play_arrow)
                }
            }
        }
    }

    class Preparing : PlayerScreenState() {
        override fun render(binding: ActivityPlayerBinding) {
            binding.playPauseButton.isEnabled = true
            binding.playPauseButton.setBackgroundResource(R.drawable.ic_play_arrow)
        }
    }

    class updateTimer(private val timerValue: String) : PlayerScreenState() {
        override fun render(binding: ActivityPlayerBinding) {
            binding.playbackProgress.text = timerValue
        }
    }

    class onCompletePlaying : PlayerScreenState() {
        override fun render(binding: ActivityPlayerBinding) {
            binding.playbackProgress.text = binding.playbackProgress.resources.getText(R.string.zero_time)
            binding.playPauseButton.setBackgroundResource(R.drawable.ic_play_arrow)
        }
    }

    abstract fun render(binding: ActivityPlayerBinding)
}