package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackListVH (itemView: View): RecyclerView.ViewHolder(itemView) {

    private val songCoverImage: ImageView = itemView.findViewById(R.id.song_cover_image)
    private val songTitle: TextView = itemView.findViewById(R.id.song_title)
    private val songArtist: TextView = itemView.findViewById(R.id.song_artist)
    private val songDuration: TextView = itemView.findViewById(R.id.song_duration)

    fun bind(item: Track) {

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.image_search)
            .transform(RoundedCorners(2))
            .into(songCoverImage)

        songTitle.text = item.trackName
        songArtist.text = item.artistName
        songDuration.text = item.trackTime
    }
}