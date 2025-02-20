package com.example.playlistmaker.main.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.placeholdersActivities.MediaActivity
import com.example.playlistmaker.settings.ui.activities.SettingsActivity
import com.example.playlistmaker.search.ui.activities.SearchActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.search.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)

            startActivity(intent)
        }

        binding.media.setOnClickListener {
            val intent = Intent(
                this, MediaActivity::class.java
            )
            startActivity(intent)

        }

        binding.settings.setOnClickListener {
            val intent = Intent(
                this, SettingsActivity::class.java
            )

            startActivity(intent)
        }
    }
}