package com.practicum.playlistmaker.player.ui

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.domain.TrackModel
import com.practicum.playlistmaker.utils.DateUtils


sealed class PlayerScreenState {
    class BeginningState(
        private val track: TrackModel,
        private val currentPosition: String,
    ) :
        PlayerScreenState() {
        override fun render(binding: ActivityPlayerBinding) {
            binding.songTitle.text = track.trackName
            binding.artistName.text = track.artistName
            binding.tvDurationValue.text = track.trackTime?.let { DateUtils.formatTrackTime(it) }
            binding.tvAlbumValue.text = track.collectionName
            binding.tvReleaseYearValue.text =
                track.releaseDate?.let { DateUtils.extractReleaseYear(it) }
            binding.tvGenreValue.text = track.primaryGenreName
            binding.tvCountryValue.text = track.country

            if (track.isFavourite) {
                binding.favoriteButton.setBackgroundResource(R.drawable.ic_favorite_selected)
            } else {
                binding.favoriteButton.setBackgroundResource(R.drawable.ic_favorite)
            }

            Glide
                .with(binding.cover)
                .load(track.getCoverArtwork())
                .placeholder(R.drawable.crocozebra)
                .transform(
                    CenterCrop(),
                    RoundedCorners(binding.cover.resources.getDimensionPixelSize(R.dimen.big_cover_corner_radius))
                )
                .into(binding.cover)

            binding.playbackProgress.text = currentPosition

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

    class FavouritesButtonHandling(val isFavourite: Boolean) : PlayerScreenState() {
        override fun render(binding: ActivityPlayerBinding) {
            if (isFavourite) {
                binding.favoriteButton.setBackgroundResource(R.drawable.ic_favorite_selected)
            } else {
                binding.favoriteButton.setBackgroundResource(R.drawable.ic_favorite)
            }
        }
    }


    class Preparing(private val trackModel: TrackModel) : PlayerScreenState() {
        override fun render(binding: ActivityPlayerBinding) {
            binding.playPauseButton.isEnabled = true
            binding.playPauseButton.setBackgroundResource(R.drawable.ic_play_arrow)
            val isFavourite = trackModel.isFavourite
            if (isFavourite) {
                binding.favoriteButton.setBackgroundResource(R.drawable.ic_favorite_selected)
            } else {
                binding.favoriteButton.setBackgroundResource(R.drawable.ic_favorite)
            }
        }
    }

    class OnCompletePlaying : PlayerScreenState() {
        override fun render(binding: ActivityPlayerBinding) {
            binding.playbackProgress.text =
                binding.playbackProgress.resources.getText(R.string.zero_time)
            binding.playPauseButton.setBackgroundResource(R.drawable.ic_play_arrow)
        }
    }

    abstract fun render(binding: ActivityPlayerBinding)
}