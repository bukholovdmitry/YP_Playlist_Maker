package com.example.yp_playlist_maker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yp_playlist_maker.databinding.TrackItemBinding
import com.example.yp_playlist_maker.search.domain.Track

class TrackAdapter(
    private val onClickListener: (Track) -> Unit,
    private val onLongClickListener: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {
    constructor(onClickListener: (Track) -> Unit) : this(onClickListener, { })

    lateinit var tracks: ArrayList<Track>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = TrackItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        val onClickListener = { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                tracks.getOrNull(position)?.let { product ->
                    onClickListener(tracks[position])
                }
            }
        }

        val onLongClickListener = { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                tracks.getOrNull(position)?.let { product ->
                    onLongClickListener(tracks[position])
                }
            }
        }
        return TrackViewHolder(binding, onClickListener, onLongClickListener)

    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        tracks.getOrNull(position)?.let {
            holder.bind(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}