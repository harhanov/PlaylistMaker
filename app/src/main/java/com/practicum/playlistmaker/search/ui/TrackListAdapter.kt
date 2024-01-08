package com.practicum.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.data.model.Track

class TrackListAdapter : RecyclerView.Adapter<TrackListVH>() {

    private val tracks = mutableListOf<Track>()
    var onClickListener: ((Track) -> Unit)? = null
    var onLongClickListener: ((Track) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return TrackListVH(view)
    }

    override fun onBindViewHolder(holder: TrackListVH, position: Int) {
        val track = tracks[position]
        if (track.isFavourite) {
            holder.favoriteButton?.setBackgroundResource(R.drawable.ic_favorite_selected)
        } else {
            holder.favoriteButton?.setBackgroundResource(R.drawable.ic_favorite)
        }
        holder.bind(track)
        holder.itemView.setOnClickListener {
            onClickListener?.invoke(track)
        }
        holder.itemView.setOnLongClickListener{
            onLongClickListener?.invoke(track)
            true
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun setTracks(newTracks: List<Track>) {
        tracks.clear()
        if (!newTracks.isNullOrEmpty()) {
            tracks.addAll(newTracks)
        }
        notifyDataSetChanged()
    }

}