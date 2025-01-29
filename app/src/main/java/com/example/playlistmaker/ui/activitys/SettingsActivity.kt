package com.example.playlistmaker.ui.activitys

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.App
import com.google.android.material.appbar.MaterialToolbar


const val THEME_PREFERENCES = "theme_preferences"
const val TYPE = "theme"

class SettingsActivity : AppCompatActivity() {
    private lateinit var btnBack: MaterialToolbar
    private lateinit var btnShare: Button
    private lateinit var btnSupport: Button
    private lateinit var btnAgreement: Button

    private lateinit var themeSwitcher: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPrefs = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)

        btnBack = findViewById(R.id.back)
        btnShare = findViewById(R.id.btnShare)
        btnSupport = findViewById(R.id.btnSupport)
        btnAgreement = findViewById(R.id.btnAgreement)

        themeSwitcher = findViewById<Switch>(R.id.themeSwitch)

        themeSwitcher.isChecked = sharedPrefs.getBoolean(TYPE, false)

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit().putBoolean(TYPE, checked).apply()
        }


        btnBack.setNavigationOnClickListener {
            finish()
        }

        btnShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plane"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.share_link)
            )

            startActivity(Intent.createChooser(intent, "Share link"))
        }

        btnSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail)))
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.mail_subject)
            )
            intent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.mail_text)
            )

            startActivity(intent)
        }

        btnAgreement.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.agreement_link)))

            startActivity(intent)
        }
    }
}