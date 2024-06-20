package com.example.yp_playlist_maker.media.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.FragmentPlaylistInfoBinding
import com.example.yp_playlist_maker.media.data.PlaylistInfoState
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.viewmodel.PlaylistInfoViewModel
import com.example.yp_playlist_maker.search.domain.Track
import com.example.yp_playlist_maker.search.ui.TrackAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class PlaylistInfoFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistInfoBinding
    private val playlistInfoViewModel: PlaylistInfoViewModel by viewModel()
    private lateinit var trackAdapter: TrackAdapter

    companion object {
        private const val SELECTED_PLAYLIST_KEY = "SELECTED_PLAYLIST"
    }

    private fun render(playlistInfo: PlaylistInfoState) {
        binding.textViewPlaylistName.text = playlistInfo.playlist.playlistName
        binding.textViewPlaylistDescription.text = playlistInfo.playlist.playlistDescription
        binding.textViewPlaylistNameItemMini.text = playlistInfo.playlist.playlistName

        Glide.with(binding.root)
            .load(playlistInfo.playlist.pathImage)
            .apply(RequestOptions().placeholder(R.drawable.placeholder))
            .into(binding.imageViewPlaylist)

        Glide.with(binding.root)
            .load(playlistInfo.playlist.pathImage)
            .apply(RequestOptions().placeholder(R.drawable.placeholder))
            .transform(
                CenterCrop(),
                RoundedCorners(
                    resources.getDimension(R.dimen.track_item_art_round_corner).roundToInt()
                )
            )
            .into(binding.imageViewPlaylistItemMini)

        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(playlistInfo.tracks)
        trackAdapter.notifyDataSetChanged()
        val sumTrackTime = SimpleDateFormat(
            "mm",
            Locale.getDefault()
        ).format(playlistInfo.tracks.map { it.trackTime }.sum()).toInt()
        binding.imageViewDot.isVisible = playlistInfo.tracks.isNotEmpty()
        binding.textViewPlaylistCountTracks.isVisible = playlistInfo.tracks.isNotEmpty()
        if (playlistInfo.tracks.isEmpty()) {
            binding.textViewPlaylistSumMinutes.text = getString(R.string.playlist_empty)
            binding.textViewPlaylistTrackCountItemMini.text = getString(R.string.playlist_empty)
        } else {
            binding.textViewPlaylistSumMinutes.text =
                binding.root.context.resources.getQuantityString(
                    R.plurals.count_of_minutes,
                    sumTrackTime,
                    sumTrackTime
                )
            binding.textViewPlaylistCountTracks.text =
                binding.root.context.resources.getQuantityString(
                    R.plurals.count_of_track_numbers,
                    playlistInfo.tracks.count(),
                    playlistInfo.tracks.count()
                )
            binding.textViewPlaylistTrackCountItemMini.text =
                binding.root.context.resources.getQuantityString(
                    R.plurals.count_of_track_numbers,
                    playlistInfo.tracks.count(),
                    playlistInfo.tracks.count()
                )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistInfoBinding.inflate(layoutInflater)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view).visibility =
            View.GONE


        val onClickListener = { track: Track ->
            val direction =
                PlaylistInfoFragmentDirections.actionPlaylistInfoFragmentToAudioPlayerFragment(track)
            findNavController().navigate(directions = direction)
        }

        trackAdapter = TrackAdapter(onClickListener) {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(getString(R.string.want_delete))
                .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    playlistInfoViewModel.deleteTrackFromPlaylist(it)
                }
                .setNeutralButton(getString(R.string.no)) { dialog, which ->
                }
                .show()
        }
        trackAdapter.tracks = ArrayList()
        binding.rvTracks.adapter = trackAdapter

        playlistInfoViewModel.observeState().observe(requireActivity()) {
            render(it)
        }

        val playlist: Playlist? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) requireArguments().getParcelable(
                SELECTED_PLAYLIST_KEY,
                Playlist::class.java
            ) else
                requireArguments().getParcelable<Playlist>(
                    SELECTED_PLAYLIST_KEY
                )

        if (playlist != null) {
            playlistInfoViewModel.loadPlaylistInfo(playlist)
        } else {
            findNavController().popBackStack()
        }

        val bottomSheetBehavior =
            BottomSheetBehavior.from(binding.playlistInfoTracksBottomSheet).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

        val bottomSheetMenuBehavior =
            BottomSheetBehavior.from(binding.playlistInfoTracksBottomSheetMenu).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }


        binding.textViewBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.playlistShare.setOnClickListener {
            playlistInfoViewModel.sharePlaylist()
        }

        binding.playlistMenuShow.setOnClickListener {
            bottomSheetMenuBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.textViewShare.setOnClickListener {
            playlistInfoViewModel.sharePlaylist()
            bottomSheetMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.textViewDeletePlaylist.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(
                    getString(R.string.want_delete_playlist)
                        .format(binding.textViewPlaylistName.text)
                )
                .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    playlistInfoViewModel.deletePlaylist()
                    findNavController().popBackStack()
                }
                .setNeutralButton(getString(R.string.no)) { dialog, which ->
                }
                .show()
        }

        binding.textEditPlaylist.setOnClickListener {
            val direction =
                PlaylistInfoFragmentDirections.actionPlaylistInfoFragmentToPlaylistCreateFragment(
                    playlistInfoViewModel.getPlaylist()
                )
            findNavController().navigate(directions = direction)
        }

        return binding.root
    }
}