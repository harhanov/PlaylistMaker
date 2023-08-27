package com.practicum.playlistmaker.utils
import java.util.concurrent.TimeUnit

object DateUtils {

    fun formatTrackTime(trackTime: String): String {
        val milliseconds = trackTime.toLong()
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }


    fun extractReleaseYear(releaseDate: String): String? {
        if (releaseDate.length >= 4) {
            return releaseDate.substring(0, 4)
        }
        return null
    }
}