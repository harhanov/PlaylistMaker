package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackListAdapter: RecyclerView.Adapter<TrackListVH>() {

    private val tracks = mutableListOf<Track>()
    var onTrackClickListener: OnTrackClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return TrackListVH(view)
    }

    override fun onBindViewHolder(holder: TrackListVH, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            onTrackClickListener?.onTrackClick(track)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun setTracks(newTracks: List<Track>?) {
        tracks.clear()
        if (!newTracks.isNullOrEmpty()) {
            tracks.addAll(newTracks)
        }
        notifyDataSetChanged()
    }

    interface OnTrackClickListener {
        fun onTrackClick(track: Track)
    }
}