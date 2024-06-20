package com.example.yp_playlist_maker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yp_playlist_maker.databinding.PlaylistItemMiniBinding
import com.example.yp_playlist_maker.media.domain.db.Playlist

class PlaylistMiniAdapter(val onClickListener: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlaylistMiniViewHolder>() {
    lateinit var playlists: ArrayList<Playlist>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistMiniViewHolder {
        val binding = PlaylistItemMiniBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PlaylistMiniViewHolder(binding) { position ->
            playlists.getOrNull(position)?.let { playlist ->
                onClickListener(playlist)
            }
        }
    }

    override fun onBindViewHolder(holder: PlaylistMiniViewHolder, position: Int) {
        playlists.getOrNull(position)?.let {
            holder.bind(playlists[position])
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}