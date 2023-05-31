package com.practicum.playlistmaker.domain.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun formatTrackTime(trackTime: Long?): String {
        return if (trackTime != null) {
            val date = Date(trackTime)
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(date)
        } else {
            "00:00"
        }
    }
}