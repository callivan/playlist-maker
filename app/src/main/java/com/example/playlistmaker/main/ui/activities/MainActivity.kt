package com.example.playlistmaker.main.ui.activities


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navHost =
            supportFragmentManager.findFragmentById(binding.rootFragmentContainer.id) as NavHostFragment
        val navController = navHost.navController

        binding.bottomMenu.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.playerFragment, R.id.mediaPlaylistCreatorFragment -> {
                    binding.bottomMenu.isVisible = false
                    binding.divider.isVisible = false
                }

                else -> {
                    binding.bottomMenu.isVisible = true
                    binding.divider.isVisible = true
                }
            }
        }
    }
}