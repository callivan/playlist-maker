package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSearch = findViewById<Button>(R.id.search)
        val btnMedia = findViewById<Button>(R.id.media)
        val btnSettings = findViewById<Button>(R.id.settings)

        val btnSearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(v?.context, SearchActivity::class.java)

                startActivity(intent)
            }
        }

        btnSearch.setOnClickListener(btnSearchClickListener)

        btnMedia.setOnClickListener {
            finish()
        }

        btnSettings.setOnClickListener {
            val intent = Intent(
                this, SettingsActivity::class.java
            )

            startActivity(intent)
        }
    }
}