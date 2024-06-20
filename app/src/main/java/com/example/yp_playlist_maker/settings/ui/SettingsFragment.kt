package com.example.yp_playlist_maker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.FragmentSettingsBinding
import com.example.yp_playlist_maker.settings.view_model.SettingsViewModel
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    private val viewModel: SettingsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchDarkTheme.isChecked = viewModel.getSavedTheme()
        binding.switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTheme(isChecked)
        }

        binding.textViewShare.setOnClickListener {
            viewModel.shareApp()
        }

        binding.textViewSendToSupport.setOnClickListener {
            viewModel.openSupport()
        }

        binding.textViewUserAgreement.setOnClickListener {
            //viewModel.openTerms()
            val direction = SettingsFragmentDirections.actionSettingsFragmentToWebFragment(
                getString(
                    R.string.uri_user_agreement
                )
            )

            findNavController().navigate(direction)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}