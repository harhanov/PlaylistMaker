package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.search.data.model.Track
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R

class TrackListAdapter(private val onClickListener: OnTrackClickListener): RecyclerView.Adapter<TrackListVH>() {

    private val tracks = mutableListOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return TrackListVH(view)
    }

    override fun onBindViewHolder(holder: TrackListVH, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            onClickListener.onTrackClick(track)
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

    fun interface OnTrackClickListener {
        fun onTrackClick(track: Track)
    }
}