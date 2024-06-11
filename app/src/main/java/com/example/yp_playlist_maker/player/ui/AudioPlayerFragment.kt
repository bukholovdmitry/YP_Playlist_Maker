package com.example.yp_playlist_maker.player.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.FragmentAudioPlayerBinding
import com.example.yp_playlist_maker.media.ui.FavoriteTracksFragment
import com.example.yp_playlist_maker.media.ui.PlaylistMiniAdapter
import com.example.yp_playlist_maker.player.data.PlayerState
import com.example.yp_playlist_maker.player.data.PlaylistsState
import com.example.yp_playlist_maker.player.ui.view_model.AudioPlayerViewModel
import com.example.yp_playlist_maker.search.domain.Track
import com.example.yp_playlist_maker.search.ui.SearchFragmentDirections
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class AudioPlayerFragment : Fragment() {
    private lateinit var binding: FragmentAudioPlayerBinding
    private val viewModel: AudioPlayerViewModel by inject()
    private lateinit var playlistMiniAdapter: PlaylistMiniAdapter
    companion object {
        private const val SELECTED_TRACK_KEY = "SELECTED_TRACK"
         fun newInstance(trackString: String) =
            FavoriteTracksFragment().apply {
                Log.d("SearchActivity", "Open AudioPlayerActivity")
                val direction = SearchFragmentDirections.actionSearchFragmentToAudioPlayerFragment(trackString)
                findNavController().navigate(R.id.audioPlayerFragment)
            }
    }

    private fun pausePlayer() {
        binding.imageViewPlay.setImageResource(R.drawable.play)
    }

    private fun startPlayer(currentPosition: String) {
        binding.imageViewPlay.setImageResource(R.drawable.pause)
        binding.tvCurrentTrackTime.text = currentPosition
    }

    private fun resumePlayer(track: Track, currentPosition: String) {
        fillData(track)
        binding.imageViewPlay.setImageResource(R.drawable.play)
        binding.tvCurrentTrackTime.text = currentPosition
    }

    private fun fillData(track: Track){
        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName
        binding.tvSelectedTrackDuration.text = SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(track.trackTime)
        binding.tvSelectedTrackPrimaryGenreName.text = track.primaryGenreName
        binding.tvSelectedTrackCountry.text = track.country
        binding.tvSelectedTrackCollectionName.text = track.collectionName
        binding.tvSelectedTrackReleaseDate.text = track.releaseDate.toString()
        Glide.with(this).load(Uri.parse(track.getCoverArtwork())).fitCenter()
            .placeholder(R.drawable.placeholder).apply(
                RequestOptions.bitmapTransform(
                    RoundedCorners(
                        this.resources.getDimension(
                            R.dimen.audio_player_track_photo_round_corner
                        ).roundToInt()
                    )
                )
            ).into(binding.ivTrackPhoto)

        if(track.isFavorite){
            binding.imageViewLike.setImageResource(R.drawable.liked)
        }
        else{
            binding.imageViewLike.setImageResource(R.drawable.like)
        }
    }

    private fun render(playerState: PlayerState) {
        when (playerState) {
            is PlayerState.StateDefault -> {
                fillData(playerState.track)
            }

            is PlayerState.StatePlaying -> {
                startPlayer(playerState.currentPosition)
            }

            is PlayerState.Pause -> {
                pausePlayer()
            }

            is PlayerState.Prepared -> {
                pausePlayer()
            }

            is PlayerState.Complete -> {
                binding.imageViewPlay.setImageResource(R.drawable.play)
                binding.tvCurrentTrackTime.text = playerState.startPosition
            }
            is PlayerState.Resume -> {
                resumePlayer(playerState.track, playerState.currentPosition)
            }
        }
    }

    private fun renderPlaylists(playlistsState: PlaylistsState){
        when(playlistsState){
            is PlaylistsState.StateData -> {
                with(playlistMiniAdapter) {
                    playlists.clear()
                    playlists.addAll(playlistsState.playlists)
                    notifyDataSetChanged()
                }
            }
            is PlaylistsState.StateMakeMessage -> {
                Log.d("AudioPlayerActivity", "Make toast %s".format(playlistsState.message))
                Toast.makeText(requireContext(), playlistsState.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentAudioPlayerBinding.inflate(layoutInflater)
        viewModel.loadTrack(stringExtra = requireArguments().getString(SELECTED_TRACK_KEY)!!)

        viewModel.observeState().observe(requireActivity()) {
            render(it)
        }

        viewModel.observePlaylistState().observe(requireActivity()){
            renderPlaylists(it)
        }

        binding.imageViewPlay.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.buttonBackFromAudioPlayer.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imageViewLike.setOnClickListener {
            viewModel.likeTrack()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
            }
        })

        playlistMiniAdapter = PlaylistMiniAdapter{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.addToPlaylist(it)
        }
        playlistMiniAdapter.playlists = ArrayList()
        binding.rvPlaylistsMini.adapter = playlistMiniAdapter
        binding.addToPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            viewModel.showPlaylists()
        }

        binding.buttonAddToPlaylist.setOnClickListener {
            findNavController().navigate(R.id.playlistCreateFragment)
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onResume() {
        Log.d("RESUME", "RESUME")
        super.onResume()
    }

    override fun onDestroy() {
        viewModel.release()
        super.onDestroy()
    }
}
