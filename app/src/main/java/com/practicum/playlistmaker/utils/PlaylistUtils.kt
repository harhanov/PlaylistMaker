package com.practicum.playlistmaker.utils

import android.content.res.Resources
import com.practicum.playlistmaker.R

object PlaylistUtils {

    fun formatNumberOfTracks(numberOfTracks: Int, resources: Resources): String {
        val tracksWord: String
        val lastDigit = numberOfTracks % 10
        val lastTwoDigits = numberOfTracks % 100

        tracksWord = if (lastTwoDigits in 11..19) {
            resources.getString(R.string.tracks)
        } else {
            when (lastDigit) {
                1 -> resources.getString(R.string.track)
                in 2..4 -> resources.getString(R.string.tracks_genitive)
                else -> resources.getString(R.string.tracks)
            }
        }

        return "$numberOfTracks $tracksWord"
    }

    fun getMinutesWord(minutes: Long): String {
        return when {
            (minutes % 10).toInt() == 1 && (minutes % 100).toInt() != 11 -> "минута"
            minutes % 10 in 2..4 && minutes % 100 !in 12..14 -> "минуты"
            else -> "минут"
        }
    }

}