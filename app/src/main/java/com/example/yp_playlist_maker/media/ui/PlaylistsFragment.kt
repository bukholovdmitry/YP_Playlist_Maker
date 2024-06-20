package com.example.yp_playlist_maker.media.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.FragmentPlaylistsBinding
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.viewmodel.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistsFragment : Fragment() {
    private lateinit var playlistAdapter: PlaylistAdapter
    private val playlistsViewModel: PlaylistsViewModel by viewModel<PlaylistsViewModel>()
    private lateinit var binding: FragmentPlaylistsBinding

    private fun render(result: Result<List<Playlist>>) {
        if (result.isSuccess) {
            val listOfPlaylists: List<Playlist>? = result.getOrNull()

            if (listOfPlaylists.isNullOrEmpty()) {
                with(playlistAdapter) {
                    playlists.clear()
                    notifyDataSetChanged()
                }
                binding.ivSadFace.visibility = View.VISIBLE
                binding.tvPlaylistsNotCreated.visibility = View.VISIBLE
            } else {
                binding.ivSadFace.visibility = View.GONE
                binding.tvPlaylistsNotCreated.visibility = View.GONE
                with(playlistAdapter) {
                    playlists.clear()
                    playlists.addAll(listOfPlaylists)
                    notifyDataSetChanged()

                    if (result.getOrNull() != null) {
                        playlists.clear()
                        playlists.addAll(listOfPlaylists)
                        notifyDataSetChanged()

                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        playlistsViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.buttonNewPlaylist.setOnClickListener {
            Log.d("PlaylistFragment", "Opening PlaylistCreateFragment")
            findNavController().navigate(R.id.playlistCreateFragment)
        }
        playlistAdapter = PlaylistAdapter {
            val direction = MediaFragmentDirections.actionMediaFragmentToPlaylistInfoFragment(it)
            findNavController().navigate(directions = direction)
        }

        playlistAdapter.playlists = ArrayList()
        binding.rvPlaylists.adapter = playlistAdapter
        playlistsViewModel.fillData()

        return binding.root
    }

    companion object {
        fun newInstance() =
            PlaylistsFragment().apply {
            }
    }
}