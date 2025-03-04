package com.example.playlistmaker.root.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityRootBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.root_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.search.setOnClickListener {
            runOnUiThread { navController.navigate(R.id.searchFragment) }
        }

        binding.media.setOnClickListener {
            navController.navigate(R.id.mediaFragment)
        }

        binding.settings.setOnClickListener {
            navController.navigate(R.id.settingsFragment)
        }
    }
}