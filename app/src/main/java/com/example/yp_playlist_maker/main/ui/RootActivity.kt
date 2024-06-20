package com.example.yp_playlist_maker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnItemSelectedListener {
            true
        }
        bottomNavigationView.selectedItemId = R.id.media_fragment
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.search_fragment -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }

                R.id.media_fragment -> {
                    navController.navigate(R.id.mediaFragment)
                    true
                }

                R.id.settings_fragment -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }

                else -> {
                    true
                }
            }
        }
    }
}