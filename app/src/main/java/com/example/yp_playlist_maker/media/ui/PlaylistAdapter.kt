package com.example.yp_playlist_maker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yp_playlist_maker.databinding.PlaylistItemBinding
import com.example.yp_playlist_maker.media.domain.db.Playlist

class PlaylistAdapter(
    val onClickListener: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistViewHolder>() {
    lateinit var playlists: ArrayList<Playlist>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PlaylistViewHolder(binding) {
            playlists.getOrNull(it)?.let { playlist ->
                onClickListener(playlist)
            }
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        playlists.getOrNull(position)?.let {
            holder.bind(playlists[position])
        }
    }
}