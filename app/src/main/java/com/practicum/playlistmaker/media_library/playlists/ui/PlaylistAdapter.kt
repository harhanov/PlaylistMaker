package com.practicum.playlistmaker.media_library.playlists.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel

class PlaylistAdapter(
    private val context: Context,
) : RecyclerView.Adapter<PlaylistVH>() {

    private var playlists = mutableListOf<PlaylistModel>()
    private var playlistClickListener: ((PlaylistModel) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistVH(view)
    }

    override fun onBindViewHolder(holder: PlaylistVH, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist, context.resources)

        holder.itemView.setOnClickListener {
            playlistClickListener?.let { it1 -> it1(playlist) }
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun setPlaylistList(playlistList: List<PlaylistModel>) {
        this.playlists = playlistList.toMutableList()
        notifyDataSetChanged()

    }
}