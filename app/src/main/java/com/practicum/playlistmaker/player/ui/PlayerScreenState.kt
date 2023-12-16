package com.practicum.playlistmaker.player.ui

import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.player.domain.TrackModel
import com.practicum.playlistmaker.utils.DateUtils
import java.io.Serializable


sealed class PlayerScreenState {
    class BeginningState(
        private val track: TrackModel,
        private val currentPosition: String,
    ) :
        PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
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
        override fun render(binding: FragmentPlayerBinding) {
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
        override fun render(binding: FragmentPlayerBinding) {
            if (isFavourite) {
                binding.favoriteButton.setBackgroundResource(R.drawable.ic_favorite_selected)
            } else {
                binding.favoriteButton.setBackgroundResource(R.drawable.ic_favorite)
            }
        }
    }


    class Preparing(private val trackModel: TrackModel) : PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
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

    object OnCompletePlaying : PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.playbackProgress.text =
                binding.playbackProgress.resources.getText(R.string.zero_time)
            binding.playPauseButton.setBackgroundResource(R.drawable.ic_play_arrow)
        }
    }

    class ShowPlaylistBottomSheet(private val bottomSheetState: BottomSheetState) :
        PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetMenu)
            when (bottomSheetState) {
                is BottomSheetState.Hidden -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    binding.overlay.visibility = View.GONE
                }

                is BottomSheetState.Collapsed -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    binding.overlay.visibility = View.VISIBLE
                }

                is BottomSheetState.Expanded -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    binding.overlay.visibility = View.VISIBLE
                }
            }
        }
    }

    data class TrackAddedToPlaylist(val playlistName: String) : PlayerScreenState() {

        override fun render(binding: FragmentPlayerBinding) {
            showToast(
                binding,
                binding.root.context.getString(R.string.added_to_playlist, playlistName)
            )
        }
    }

    data class TrackAlreadyInPlaylist(val playlistName: String) : PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            showToast(
                binding,
                binding.root.context.getString(R.string.has_already_been_added, playlistName)
            )
        }
    }

    fun showToast(binding: FragmentPlayerBinding, message: String) {
        Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
    }

    sealed class PlayerEvent {
        object NavigateBackToPlayerFragment : PlayerEvent()
    }

    sealed class BottomSheetState : Serializable {
        object Hidden : BottomSheetState()
        object Collapsed : BottomSheetState()
        object Expanded : BottomSheetState()

        fun toggle(): Any {
            return when (this) {
                Hidden -> Collapsed
                Collapsed -> Hidden
                Expanded -> Collapsed
            }
        }

        companion object {
            val DEFAULT: BottomSheetState = Hidden
        }
    }

    abstract fun render(binding: FragmentPlayerBinding)
}