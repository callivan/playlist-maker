package com.example.playlistmaker.settings.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.viewModels.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.net.toUri

class SettingsFragment : Fragment() {
    private val viewModel by viewModel<SettingsViewModel>()

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getThemeLiveData().observe(viewLifecycleOwner) { isDarkTheme ->
            binding.themeSwitch.isChecked = isDarkTheme
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }

        binding.btnShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plane"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.share_link)
            )

            startActivity(
                Intent.createChooser(
                    intent,
                    "Share link"
                )
            )
        }

        binding.btnSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = "mailto:".toUri()
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

        binding.btnAgreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, getString(R.string.agreement_link).toUri())

            startActivity(intent)
        }
    }
}