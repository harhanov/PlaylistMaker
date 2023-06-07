package com.practicum.playlistmaker.ui.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.utils.DateUtils


class TrackListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val songCoverImage: ImageView = itemView.findViewById(R.id.song_cover_image)
    private val songTitle: TextView = itemView.findViewById(R.id.song_title)
    private val songArtist: TextView = itemView.findViewById(R.id.song_artist)
    private val songDuration: TextView = itemView.findViewById(R.id.song_duration)

    fun bind(item: Track) {
        val radiusInPixels = itemView.resources.getDimensionPixelSize(R.dimen.cover_corner_radius)

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.crocozebra)
            .transform(RoundedCorners(radiusInPixels))
            .into(songCoverImage)

        songTitle.text = item.trackName
        songArtist.text = item.artistName
        songDuration.text = DateUtils.formatTrackTime(item.trackTime)

    }

}