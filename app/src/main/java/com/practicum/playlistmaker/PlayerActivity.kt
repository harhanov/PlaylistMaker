package com.practicum.playlistmaker

import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.utils.DateUtils.formatTrackTime
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val CURRENT_POSITION_REFRESH = 300L
    }

    private lateinit var trackNameTextView: TextView
    private lateinit var artistNameTextView: TextView
    private lateinit var trackTimeTextView: TextView
    private lateinit var artworkImageView: ImageView
    private lateinit var collectionNameTextView: TextView
    private lateinit var releaseDateTextView: TextView
    private lateinit var primaryGenreNameTextView: TextView
    private lateinit var countryTextView: TextView

    private lateinit var play: Button
    private lateinit var currentPosition: TextView
    private lateinit var playRunnable: Runnable
    private lateinit var timerRunnable: Runnable
    private lateinit var handler: Handler
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backFromPlayer = findViewById<ImageView>(R.id.playerBackButton)

        backFromPlayer.setOnClickListener {
            mediaPlayer.release()
            finish()
        }

        play = findViewById(R.id.playback_button)
        currentPosition = findViewById(R.id.playback_progress)
        val url = intent.getStringExtra("previewUrl") ?: ""
        preparePlayer(url)

        play.setOnClickListener {
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

        playRunnable = Runnable { playbackControl() }
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
        if (mediaPlayer.isPlaying || (playerState == STATE_PAUSED))
         {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }

    private fun setCurrentPosition() {
        if (playerState == STATE_PREPARED || playerState == STATE_PLAYING) {
                currentPosition.text = formatTrackTime(mediaPlayer.currentPosition.toLong())
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
        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnErrorListener { mp, what, extra ->
                // Обработка ошибки
                Log.e("PlayerActivity", "Ошибка при подготовке плеера: $what, $extra")
                // Дополнительные действия, если необходимо

                // Возвращаем true, чтобы указать, что ошибка была обработана
                true
            }
            mediaPlayer.setOnPreparedListener {
                play.isEnabled = true
                playerState = STATE_PREPARED
                Log.d("PlayerActivity", "Плеер подготовлен")
            }
            mediaPlayer.setOnCompletionListener {
                play.text = "PLAY"
                playerState = STATE_PREPARED
            }
        } catch (e: FileNotFoundException) {
            Log.e("PlayerActivity", "Файл не найден: $url")
            e.printStackTrace()
        } catch (e: IOException) {
            Log.e("PlayerActivity", "Ошибка ввода-вывода при подготовке плеера")
            e.printStackTrace()
        }
    }

    private fun startPlayer() {
        currentPosition.text = getString(R.string.zero_time)
        mediaPlayer.start()
        play.text = "PAUSE"
        playerState = STATE_PLAYING
        timerUpdate(CURRENT_POSITION_REFRESH)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play.text = "PLAY"
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
                play.isEnabled = true
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

    private fun resetTimer() {
        if (::timerRunnable.isInitialized) {
            handler.removeCallbacks(timerRunnable)
        }
        if (::playRunnable.isInitialized) {
            handler.removeCallbacks(playRunnable)
        }
    }


    private fun timerUpdate(seconds: Long) {
        if (playerState == STATE_PREPARED || playerState == STATE_PLAYING) {
            setCurrentPosition()
            timerRunnable = Runnable { timerUpdate(seconds) }
            handler.postDelayed(timerRunnable, CURRENT_POSITION_REFRESH)
        }
    }
}
