package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackListAdapter() : RecyclerView.Adapter<TrackListVH> () {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return TrackListVH(view)
    }

    override fun onBindViewHolder(holder: TrackListVH, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun setTracks(newTracks: List<Track>?){
        tracks.clear()
        if (!newTracks.isNullOrEmpty()){
            tracks.addAll(newTracks)
        }
        notifyDataSetChanged()
    }

}