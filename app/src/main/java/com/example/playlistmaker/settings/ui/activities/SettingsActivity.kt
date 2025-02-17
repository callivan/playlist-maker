package com.example.playlistmaker.settings.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.viewModels.SettingsViewModel

class SettingsActivity : ComponentActivity() {
    private val viewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.getViewModelFactory(this)
    }

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.themeSwitch.isChecked = viewModel.isDarkTheme()

        binding.themeSwitch.setOnCheckedChangeListener { _, checked -> viewModel.switchTheme(checked) }


        binding.back.setNavigationOnClickListener {
            finish()
        }

        binding.btnShare.setOnClickListener {
            viewModel.shareLink(getString(R.string.share_link))
        }

        binding.btnSupport.setOnClickListener {
            viewModel.sendEmail(
                mail = getString(R.string.mail),
                title = getString(R.string.mail_subject),
                text = getString(R.string.mail_text)
            )
        }

        binding.btnAgreement.setOnClickListener {
            viewModel.linTo(getString(R.string.agreement_link))
        }
    }
}