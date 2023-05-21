package com.practicum.playlistmaker

import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.utils.DateUtils.formatTrackTime


class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val CURRENT_POSITION_REFRESH = 200L
    }

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

    private lateinit var timerRunnable: Runnable
    private lateinit var handler: Handler
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var isPlaying = false
    private val playIcon = R.drawable.ic_play_arrow
    private val pauseIcon = R.drawable.pause_button
    private var currentPositionMillis: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backFromPlayer = findViewById<ImageView>(R.id.playerBackButton)

        backFromPlayer.setOnClickListener {
            mediaPlayer.release()
            finish()
        }

        playPauseButton = findViewById(R.id.playPauseButton)
        playPauseButton.setBackgroundResource(playIcon)

        if (savedInstanceState != null) {
            isPlaying = savedInstanceState.getBoolean("isPlaying", false)
            currentPositionMillis = savedInstanceState.getInt("currentPositionMillis", 0)
        }

        currentPositionView = findViewById(R.id.playback_progress)
        val url = intent.getStringExtra("previewUrl") ?: ""
        preparePlayer(url)

        playPauseButton.setOnClickListener {
            playbackControl()
        }

        val artworkUrl100 = intent.getStringExtra("artworkUrl100")
        val coverArtworkUrl = artworkUrl100?.let { getCoverArtwork(it) }
        val trackName = intent.getStringExtra("trackName")
        val artistName = intent.getStringExtra("artistName")
        val trackTime = intent.getStringExtra("trackTime")
        val collectionName = intent.getStringExtra("collectionName")
        val releaseDate = extractReleaseYear(intent)
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")

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
        trackTimeTextView.text = formatTrackTime(trackTime?.toLong())
        collectionNameTextView.text = collectionName
        releaseDateTextView.text = releaseDate
        primaryGenreNameTextView.text = primaryGenreName
        countryTextView.text = country

        handler = Handler(Looper.getMainLooper())

        val radiusInPixels = this.resources.getDimensionPixelSize(R.dimen.big_cover_corner_radius)

        Glide.with(this)
            .load(coverArtworkUrl)
            .centerCrop()
            .placeholder(R.drawable.crocozebra)
            .transform(RoundedCorners(radiusInPixels))
            .into(artworkImageView)
    }

    override fun onStop() {
        super.onStop()
        resetTimer()
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
        } catch (_: IllegalStateException) {
        }
    }

    private fun getCoverArtwork(artworkUrl100: String): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }

    private fun extractReleaseYear(intent: Intent): String? {
        val releaseDate = intent.getStringExtra("releaseDate")?.let {
            if (it.length >= 4) {
                it.substring(0, 4)
            } else {
                it
            }
        }
        return releaseDate
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.apply {
            setDataSource(url)
            setOnPreparedListener {
                playerState = STATE_PREPARED
                playPauseButton.setBackgroundResource(playIcon)
                currentPositionView.text = formatTrackTime(currentPositionMillis.toLong())
            }
            setOnCompletionListener {
                playerState = STATE_PREPARED
                playPauseButton.setBackgroundResource(playIcon)
                currentPositionMillis = 0
                currentPositionView.text = getString(R.string.zero_time)
            }
            prepareAsync()
        }
    }

    private fun startPlayer() {
        playPauseButton.setBackgroundResource(pauseIcon)
        if (currentPositionMillis == mediaPlayer.duration) {
            currentPositionMillis = 0
        }
        mediaPlayer.seekTo(currentPositionMillis)
        mediaPlayer.start()
        playerState = STATE_PLAYING
        isPlaying = true
        updateCurrentPosition()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                onPause()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        resetTimer()
        mediaPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        if (!isMediaPlayerReleased() && mediaPlayer.isPlaying) {
            currentPositionMillis = mediaPlayer.currentPosition
            pauseMediaPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isMediaPlayerReleased() && !mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(currentPositionMillis)
            pauseMediaPlayer()
        }
    }

    private fun pauseMediaPlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        playPauseButton.setBackgroundResource(playIcon)
        isPlaying = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isPlaying", isPlaying)
        outState.putInt("currentPositionMillis", currentPositionMillis)
    }

    private fun isMediaPlayerReleased(): Boolean {
        return try {
            mediaPlayer.isPlaying
            false
        } catch (e: IllegalStateException) {
            true
        }
    }

    private fun resetTimer() {
        if (::timerRunnable.isInitialized) {
            handler.removeCallbacks(timerRunnable)
        }
    }

    private var isTimerRunning = false

    private fun updateCurrentPosition() {
        if (playerState != STATE_PLAYING) return

        try {
            val currentPositionText = formatTrackTime(mediaPlayer.currentPosition.toLong())
            currentPositionView.text = currentPositionText
            currentPositionMillis = mediaPlayer.currentPosition
        } catch (_: IllegalStateException) {
        }

        if (!isTimerRunning) {
            isTimerRunning = true
            handler.postDelayed({
                isTimerRunning = false
                updateCurrentPosition()
            }, CURRENT_POSITION_REFRESH)
        }
    }

}
