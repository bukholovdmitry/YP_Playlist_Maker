package com.example.yp_playlist_maker.sharing.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yp_playlist_maker.databinding.FragmentWebBinding

const val URI_LINK = "uri_link"

class WebFragment : Fragment() {
    private lateinit var binding: FragmentWebBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uriLink = requireArguments().getString(URI_LINK)
        if (uriLink != null) {
            binding.webView.loadUrl(uriLink)
        }

        binding.buttonBackAgreement.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}