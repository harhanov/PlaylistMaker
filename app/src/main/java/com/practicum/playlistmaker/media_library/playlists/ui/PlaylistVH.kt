package com.practicum.playlistmaker.media_library.playlists.ui

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel

class PlaylistVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val playlistCoverImage: ImageView = itemView.findViewById(R.id.playlist_cover_image)
    private val playlistName: TextView = itemView.findViewById(R.id.playlist_name)
    private val numberOfTracks: TextView = itemView.findViewById(R.id.number_of_tracks)

    fun bind(item: PlaylistModel, resources: Resources, radiusInPixels: Int) {
        val radius = resources.getDimension(radiusInPixels).toInt()
        playlistName.text = item.playlistName
        numberOfTracks.text = formatNumberOfTracks(item.numberOfTracks, resources)

        Glide.with(itemView)
            .load(item.playlistImagePath)
            .placeholder(R.drawable.crocozebra)
            .transform(
                CenterCrop(),
                RoundedCorners(radius)
            )
            .into(playlistCoverImage)
    }

    private fun formatNumberOfTracks(numberOfTracks: Int, resources: Resources): String {
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

}