package com.practicum.playlistmaker.media_library.playlists.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel

class PlaylistAdapter(
    private val context: Context,
    private val layoutResourceId: Int,
    private val radiusInPixels: Int,
    private var clickerListener: Clicker? = null,
) : RecyclerView.Adapter<PlaylistVH>() {

    private var playlists = mutableListOf<PlaylistModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistVH {
        Log.d("PlayerFragment", "onCreateViewHolder: $viewType")
        val view = LayoutInflater.from(parent.context).inflate(layoutResourceId, parent, false)
        return PlaylistVH(view)
    }

    override fun onBindViewHolder(holder: PlaylistVH, position: Int) {
        Log.d("PlayerFragment", "onBindViewHolder: $position")
        val playlist = playlists[position]
        holder.bind(playlist, context.resources, radiusInPixels)

        holder.itemView.setOnClickListener {
            Log.d("ClickCheck", "Нажали на плейлист ${playlist.playlistName}")
            clickerListener?.onClick(playlist)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun setPlaylistsList(playlistsList: List<PlaylistModel>) {
        Log.d("PlayerFragment", "setPlaylistsList - size: ${playlistsList.size}")
        playlists.clear()
        playlists.addAll(playlistsList)
        notifyItemRangeChanged(0, playlists.size)
    }

    fun setClickerListener(listener: Clicker?) {
        clickerListener = listener
    }
    interface Clicker {
        fun onClick(playlist: PlaylistModel)
    }
}
