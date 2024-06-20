package com.example.yp_playlist_maker.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.FragmentSearchBinding
import com.example.yp_playlist_maker.search.data.SearchState
import com.example.yp_playlist_maker.search.domain.Track
import com.example.yp_playlist_maker.search.view_model.SearchViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject

class SearchFragment : Fragment() {
    private lateinit var trackAdapter: TrackAdapter
    private val viewModel: SearchViewModel by inject()
    private lateinit var textWatcher: TextWatcher
    private lateinit var binding: FragmentSearchBinding

    private fun showHistoryFeatures(isVisible: Boolean) {
        if (isVisible) {
            binding.buttonClearHistory.visibility = View.VISIBLE
            binding.tvLookingFor.visibility = View.VISIBLE
        } else {
            binding.buttonClearHistory.visibility = View.GONE
            binding.tvLookingFor.visibility = View.GONE
        }
    }

    private fun showLoading() {
        binding.flContent.removeAllViewsInLayout()
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
        showHistoryFeatures(false)
        binding.progressBarSearchTracks.visibility = View.VISIBLE
    }

    private fun showError(onClickListener: View.OnClickListener) {
        binding.progressBarSearchTracks.visibility = View.GONE
        layoutInflater.inflate(R.layout.fragment_no_internet, binding.flContent)
        activity?.findViewById<LinearLayout>(R.id.ll_no_internet)
            ?.findViewById<Button>(R.id.button_retry)?.setOnClickListener(onClickListener)
    }

    private fun showEmpty() {
        binding.progressBarSearchTracks.visibility = View.GONE
        binding.flContent.removeAllViewsInLayout()
        layoutInflater.inflate(R.layout.fragment_not_found, binding.flContent)
    }

    private fun showContent(newTrackList: List<Track>) {
        binding.progressBarSearchTracks.visibility = View.GONE
        with(trackAdapter) {
            tracks.clear()
            tracks.addAll(newTrackList)
            notifyDataSetChanged()
        }
    }

    private fun showHistory(historyTrackList: List<Track>) {
        binding.progressBarSearchTracks.visibility = View.GONE
        with(trackAdapter) {
            tracks.clear()
            tracks.addAll(historyTrackList)
            notifyDataSetChanged()
        }
        showHistoryFeatures(historyTrackList.isNotEmpty())
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Error -> showError(state.onClickListener)
            is SearchState.Loading -> showLoading()
            is SearchState.Empty -> showEmpty()
            is SearchState.History -> showHistory(state.tracks)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view).visibility =
            View.VISIBLE
        binding.buttonClearHistory.visibility = View.GONE

        trackAdapter = TrackAdapter {
            viewModel.onTrackClickDebounce(it)
            val direction = SearchFragmentDirections.actionSearchFragmentToAudioPlayerFragment(it)
            findNavController().navigate(directions = direction)
        }
        trackAdapter.tracks = ArrayList()
        binding.rvTracks.adapter = trackAdapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        fun clearButtonVisibility(s: CharSequence?): Int {
            return if (s.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(s.toString())
                binding.imageViewClearText.visibility = clearButtonVisibility(s)
            }
        }
        textWatcher.let { binding.editTextSearch.addTextChangedListener(textWatcher) }

        binding.editTextSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchTracks(textView.text.toString())
                true
            }
            false
        }
        binding.editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.editTextSearch.text.toString() == "") {
                showHistoryFeatures(true)
                viewModel.showTracksHistory()
                if (trackAdapter.tracks.isEmpty()) {
                    showHistoryFeatures(false)
                }
            } else {
                showHistoryFeatures(false)
            }

        }
        binding.imageViewClearText.setOnClickListener {
            binding.editTextSearch.setText("")
            val view: View? = activity?.currentFocus
            viewModel.showTracksHistory()
            showHistoryFeatures(trackAdapter.tracks.isNotEmpty())
            binding.flContent.visibility = View.GONE

            if (view != null) {
                val inputMethodManager =
                    activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearTracksHistory()
            showContent(emptyList())
            showHistoryFeatures(false)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher.let { binding.editTextSearch.removeTextChangedListener(it) }
    }
}