package com.practicum.playlistmaker.presentation.ui.player

import android.os.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.utils.DateUtils.extractReleaseYear
import com.practicum.playlistmaker.domain.utils.DateUtils.formatTrackTime
import com.practicum.playlistmaker.presentation.presenters.PlayerPresenter

class PlayerActivity : AppCompatActivity(), PlayerView {

    private lateinit var trackNameTextView: TextView
    private lateinit var artistNameTextView: TextView
    private lateinit var trackTimeTextView: TextView
    private lateinit var artworkImageView: ImageView
    private lateinit var collectionNameTextView: TextView
    private lateinit var releaseDateTextView: TextView
    private lateinit var primaryGenreNameTextView: TextView
    private lateinit var countryTextView: TextView
    private lateinit var playPauseButton: Button
    private lateinit var currentPositionView: TextView

    private val playIcon = R.drawable.ic_play_arrow
    private val pauseIcon = R.drawable.pause_button

    private lateinit var playerPresenter: PlayerPresenter
    private lateinit var track: Track


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backFromPlayer = findViewById<ImageView>(R.id.playerBackButton)

        backFromPlayer.setOnClickListener {
            finish()
        }

        playPauseButton = findViewById(R.id.playPauseButton)
        playPauseButton.setBackgroundResource(playIcon)

        currentPositionView = findViewById(R.id.playback_progress)


        playPauseButton.setOnClickListener {
            playerPresenter.playbackControl()
        }

        track = intent.getParcelableExtra("track")!!
        playerPresenter = PlayerPresenter(track, this)


        val artworkUrl100 = track.artworkUrl100
        val coverArtworkUrl = artworkUrl100.let { getCoverArtwork(it) }
        val trackName = track.trackName
        val artistName = track.artistName
        val trackTime = track.trackTime
        val collectionName = track.collectionName
        val releaseDate = track.let { it.releaseDate?.let { it1 -> extractReleaseYear(it1) } }
        val primaryGenreName = track.primaryGenreName
        val country = track.country

        trackNameTextView = findViewById(R.id.songTitle)
        artistNameTextView = findViewById(R.id.artistName)
        trackTimeTextView = findViewById(R.id.tvDurationValue)
        artworkImageView = findViewById(R.id.cover)
        collectionNameTextView = findViewById(R.id.tvAlbumValue)
        releaseDateTextView = findViewById(R.id.tvReleaseYearValue)
        primaryGenreNameTextView = findViewById(R.id.tvGenreValue)
        countryTextView = findViewById(R.id.tvCountryValue)

        trackNameTextView.text = trackName
        artistNameTextView.text = artistName
        trackTimeTextView.text = formatTrackTime(trackTime)
        collectionNameTextView.text = collectionName
        releaseDateTextView.text = releaseDate
        primaryGenreNameTextView.text = primaryGenreName
        countryTextView.text = country

        val radiusInPixels = this.resources.getDimensionPixelSize(R.dimen.big_cover_corner_radius)

        Glide.with(this)
            .load(coverArtworkUrl)
            .centerCrop()
            .placeholder(R.drawable.crocozebra)
            .transform(RoundedCorners(radiusInPixels))
            .into(artworkImageView)
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
        currentPositionView.text = timerValue
    }

    override fun setPlayButtonIcon(isPlaying: Boolean) {
        val icon = if (isPlaying) pauseIcon else playIcon
        playPauseButton.setBackgroundResource(icon)
    }

    override fun onCompletePlaying() {
        currentPositionView.text = resources.getText(R.string.zero_time)
        playerPresenter.stopPlayback()
    }

}