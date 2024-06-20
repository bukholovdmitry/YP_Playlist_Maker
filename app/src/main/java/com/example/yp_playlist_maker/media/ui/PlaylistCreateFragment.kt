package com.example.yp_playlist_maker.media.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.FragmentPlaylistCreateBinding
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.viewmodel.PlaylistCreateViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class PlaylistCreateFragment : Fragment() {
    private var _binding: FragmentPlaylistCreateBinding? = null
    private val viewModelPlaylistCreate: PlaylistCreateViewModel by viewModel()

    companion object {
        private const val IS_SAVED_INSTANCE = "IS_SAVED_INSTANCE"
        private const val SELECTED_PLAYLIST_KEY = "SELECTED_PLAYLIST"
    }

    private val binding: FragmentPlaylistCreateBinding
        get() = _binding!!

    private fun renderPlaylist(playlist: Playlist) {
        binding.editTextPlaylistName.editText?.setText(playlist.playlistName)
        binding.editTextPlaylistDescription.editText?.setText(playlist.playlistDescription)
        binding.viewAddImage.tag = playlist.pathImage
        Glide.with(binding.root)
            .load(playlist.pathImage)
            .transform(
                CenterCrop(),
                RoundedCorners(
                    binding.root.resources.getDimension(R.dimen.search_corner_radius).roundToInt()
                )
            )
            .into(binding.viewAddImage)
        if (playlist.playlistId > 0) {
            binding.buttonCreatePlaylist.setText(R.string.save)
            binding.buttonBack.setText(R.string.edit)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistCreateBinding.inflate(layoutInflater)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view).visibility =
            View.GONE

        viewModelPlaylistCreate.observeState().observe(requireActivity()) {
            renderPlaylist(it)
        }

        fun savePlaylist() {
            val playlist = Playlist(
                0,
                binding.editTextPlaylistName.editText?.text.toString(),
                binding.editTextPlaylistDescription.editText?.text.toString(),
                if (binding.viewAddImage.tag == null) "" else binding.viewAddImage.tag.toString(),
                0
            )
            viewModelPlaylistCreate.createPlaylist(playlist)
            Toast.makeText(
                context,
                getString(R.string.playlist_created).format(playlist.playlistName),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
        }

        fun showMessageDialogClose() {
            if (binding.editTextPlaylistName.editText?.text?.isNotBlank() == true
                || binding.editTextPlaylistDescription.editText?.text?.isNotBlank() == true
                || (if (binding.viewAddImage.tag == null) "" else binding.viewAddImage.tag.toString()) != ""
            ) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.finish_creating_playlist))
                    .setMessage(getString(R.string.unsaved_data_will_be_lost))
                    .setNeutralButton(getString(R.string.cancel)) { dialog, which ->
                    }
                    .setNegativeButton(getString(R.string.finish)) { dialog, which ->
                        findNavController().popBackStack()
                    }.show()
            } else {
                findNavController().popBackStack()
            }
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(IS_SAVED_INSTANCE)) {
                viewModelPlaylistCreate.loadFromSavedInstanceState()
            }
        } else {
            val playlist: Playlist? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requireArguments().getParcelable(SELECTED_PLAYLIST_KEY, Playlist::class.java)
            } else {
                requireArguments().getParcelable<Playlist>(SELECTED_PLAYLIST_KEY)
            }
            if (playlist != null) {
                viewModelPlaylistCreate.loadFromArgument(playlist)
            }
        }


        if (viewModelPlaylistCreate.currentMode()) {
            binding.buttonCreatePlaylist.setOnClickListener {
                if (binding.editTextPlaylistName.editText?.text?.isNotBlank() == true) {
                    savePlaylist()
                }
            }

            binding.buttonBack.setOnClickListener {
                showMessageDialogClose()
            }

            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        showMessageDialogClose()
                    }
                })
        } else {
            binding.buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }

            binding.buttonCreatePlaylist.setOnClickListener {
                viewModelPlaylistCreate.updatePlaylist(
                    binding.editTextPlaylistName.editText?.text.toString(),
                    binding.editTextPlaylistDescription.editText?.text.toString(),
                    if (binding.viewAddImage.tag == null) "" else binding.viewAddImage.tag.toString()
                )
                findNavController().popBackStack()
            }
        }


        binding.editTextPlaylistName.editText?.addTextChangedListener(
            onTextChanged = { charSequence, _, _, _ ->
                binding.buttonCreatePlaylist.isEnabled =
                    binding.editTextPlaylistName.editText?.text.toString().isNotBlank()
            })

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(binding.root)
                        .load(uri)
                        .transform(
                            CenterCrop(),
                            RoundedCorners(
                                binding.root.resources.getDimension(R.dimen.search_corner_radius)
                                    .roundToInt()
                            )
                        )
                        .into(binding.viewAddImage)
                    binding.viewAddImage.tag = uri.toString()
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.viewAddImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModelPlaylistCreate.savePlaylistInstanceState(
            binding.editTextPlaylistName.editText?.text.toString(),
            binding.editTextPlaylistDescription.editText?.text.toString(),
            if (binding.viewAddImage.tag == null) "" else binding.viewAddImage.tag.toString()
        )

        outState.putBoolean(IS_SAVED_INSTANCE, true)
    }
}