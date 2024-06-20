package com.example.yp_playlist_maker.media.ui

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.PlaylistItemMiniBinding
import com.example.yp_playlist_maker.media.domain.db.Playlist
import java.io.File
import kotlin.math.roundToInt

class PlaylistMiniViewHolder(
    private val binding: PlaylistItemMiniBinding,
    private val onClickListener: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        itemView.setOnClickListener {
            onClickListener(adapterPosition)
        }
    }

    @SuppressLint("ResourceType")
    fun bind(item: Playlist) {
        binding.textViewPlaylistName.text = item.playlistName
        binding.textViewPlaylistTrackCount.text = binding.root.context.resources.getQuantityString(
            R.plurals.count_of_track_numbers,
            item.countTracksInPlaylist,
            item.countTracksInPlaylist
        )
        val radiusRound =
            binding.root.resources.getDimension(R.dimen.track_item_art_round_corner).roundToInt()

        Glide.with(binding.root)
            .load(File(item.pathImage, ""))
            .apply(RequestOptions().placeholder(R.drawable.placeholder))
            .transform(CenterCrop(), RoundedCorners(radiusRound))
            .into(binding.imageViewPlaylist)
    }
}