package com.practicum.playlistmaker.media_library.playlists.ui

import android.content.Context
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
        val view = LayoutInflater.from(parent.context).inflate(layoutResourceId, parent, false)
        return PlaylistVH(view)
        }

    override fun onBindViewHolder(holder: PlaylistVH, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist, context.resources, radiusInPixels)

        holder.itemView.setOnClickListener {
            clickerListener?.onClick(playlist)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun setPlaylistsList(playlistsList: List<PlaylistModel>) {
        playlists.clear()
        playlists.addAll(playlistsList)
        notifyDataSetChanged()
    }

    fun setClickerListener(listener: Clicker?) {
        clickerListener = listener
    }
    interface Clicker {
        fun onClick(playlist: PlaylistModel)
    }
}
