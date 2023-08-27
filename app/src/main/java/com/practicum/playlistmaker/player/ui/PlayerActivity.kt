package com.practicum.playlistmaker.player.ui

import com.practicum.playlistmaker.search.domain.Track
import android.os.*
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.ui.presenters.PlayerPresenter
import com.practicum.playlistmaker.utils.DateUtils.extractReleaseYear
import com.practicum.playlistmaker.utils.DateUtils.formatTrackTime

class PlayerActivity : ComponentActivity(), PlayerView {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var playerPresenter: PlayerPresenter
    private lateinit var viewModel: PlayerViewModel
    private lateinit var track: Track

    private val playIcon = R.drawable.ic_play_arrow
    private val pauseIcon = R.drawable.pause_button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backFromPlayer = binding.playerBackButton
        val playPauseButton = binding.playPauseButton

        backFromPlayer.setOnClickListener {
            finish()
        }

        track = intent.getParcelableExtra("track")!!
        playerPresenter = PlayerPresenter(track, this)

        viewModel = ViewModelProvider(this, PlayerViewModel.getViewModelFactory(track.trackId))[PlayerViewModel::class.java]

        val artworkUrl100 = track.artworkUrl100
        val coverArtworkUrl = artworkUrl100.let { getCoverArtwork(it) }
        val trackName = track.trackName
        val artistName = track.artistName
        val trackTime = track.trackTime
        val collectionName = track.collectionName
        val releaseDate = track.let { it.releaseDate?.let { it1 -> extractReleaseYear(it1) } }
        val primaryGenreName = track.primaryGenreName
        val country = track.country

        binding.songTitle.text = trackName
        binding.artistName.text = artistName
        binding.tvDurationValue.text = formatTrackTime(trackTime)
        binding.tvAlbumValue.text = collectionName
        binding.tvReleaseYearValue.text = releaseDate
        binding.tvGenreValue.text = primaryGenreName
        binding.tvCountryValue.text = country

        val radiusInPixels = resources.getDimensionPixelSize(R.dimen.big_cover_corner_radius)

        Glide.with(this)
            .load(coverArtworkUrl)
            .centerCrop()
            .placeholder(R.drawable.crocozebra)
            .transform(RoundedCorners(radiusInPixels))
            .into(binding.cover)

        playPauseButton.setOnClickListener {
            playerPresenter.playbackControl()
        }
    }

    private fun getCoverArtwork(artworkUrl100: String): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }

    override fun onDestroy() {
        super.onDestroy()
        playerPresenter.releasePlayer()
    }

    override fun startPostDelay() {
        playerPresenter.startPostDelay()
    }

    override fun updateTimer(timerValue: String) {
        binding.playbackProgress.text = timerValue
    }

    override fun setPlayButtonIcon(isPlaying: Boolean) {
        val icon = if (isPlaying) pauseIcon else playIcon
        binding.playPauseButton.setBackgroundResource(icon)
    }

    override fun onCompletePlaying() {
        binding.playbackProgress.text = resources.getText(R.string.zero_time)
        playerPresenter.stopPlayback()
    }
}
