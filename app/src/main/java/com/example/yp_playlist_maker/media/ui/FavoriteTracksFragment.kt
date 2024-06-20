package com.example.yp_playlist_maker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yp_playlist_maker.databinding.FragmentFavoriteTracksBinding
import com.example.yp_playlist_maker.media.viewmodel.FavoriteTracksViewModel
import com.example.yp_playlist_maker.search.domain.Track
import com.example.yp_playlist_maker.search.ui.TrackAdapter
import org.koin.android.ext.android.inject

class FavoriteTracksFragment : Fragment() {
    private lateinit var trackAdapter: TrackAdapter
    private val viewModel: FavoriteTracksViewModel by inject<FavoriteTracksViewModel>()
    private lateinit var binding: FragmentFavoriteTracksBinding

    companion object {
        fun newInstance() =
            FavoriteTracksFragment().apply {
            }
    }

    private fun render(result: Result<List<Track>>) {
        if (result.isSuccess) {
            if (result.getOrNull()!!.isEmpty()) {
                with(trackAdapter) {
                    tracks.clear()
                    notifyDataSetChanged()
                }
                binding.ivSadFace.visibility = View.VISIBLE
                binding.tvPlaylistsNotCreated.visibility = View.VISIBLE

            } else {
                binding.ivSadFace.visibility = View.GONE
                binding.tvPlaylistsNotCreated.visibility = View.GONE
                with(trackAdapter) {
                    tracks.clear()
                    tracks.addAll(result.getOrNull()!!)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteTracksBinding.inflate(layoutInflater)

        trackAdapter = TrackAdapter {
            val direction = MediaFragmentDirections.actionMediaFragmentToAudioPlayerFragment(it)
            findNavController().navigate(directions = direction)
        }
        trackAdapter.tracks = ArrayList()
        binding.rvLikedTracks.adapter = trackAdapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.fillData()

        return binding.root
    }
}