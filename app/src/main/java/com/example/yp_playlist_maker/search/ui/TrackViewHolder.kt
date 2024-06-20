package com.example.yp_playlist_maker.search.ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.TrackItemBinding
import com.example.yp_playlist_maker.search.domain.Track
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class TrackViewHolder(
    private val binding: TrackItemBinding,
    onClickListener: (position: Int) -> Unit,
    onLongClickListener: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        itemView.setOnClickListener {
            onClickListener(adapterPosition)
        }

        itemView.setOnLongClickListener {
            onLongClickListener(adapterPosition)
            true
        }
    }

    @SuppressLint("ResourceType")
    fun bind(item: Track) {
        binding.textViewTrackArtist.text = item.artistName
        binding.textViewTrackName.text = item.trackName
        binding.textViewTrackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTime)

        val radiusRound =
            binding.root.resources.getDimension(R.dimen.track_item_art_round_corner).roundToInt()

        Glide.with(binding.root)
            .load(Uri.parse(item.artworkUrl100))
            .fitCenter()
            .apply(RequestOptions().placeholder(R.drawable.placeholder))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusRound)))
            .into(binding.imageViewTrackArt)
    }
}