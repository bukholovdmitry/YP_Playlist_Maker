package com.example.yp_playlist_maker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.FragmentMediaBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : Fragment() {
    private lateinit var binding: FragmentMediaBinding
    private lateinit var tabMedia: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaBinding.inflate(inflater, container, false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view).visibility =
            View.VISIBLE
        binding.pagerMedia.adapter =
            MediaViewPagerAdapter(fragmentManager = childFragmentManager, lifecycle)
        tabMedia = TabLayoutMediator(binding.tabLayoutMedia, binding.pagerMedia) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMedia.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMedia.detach()
    }
}