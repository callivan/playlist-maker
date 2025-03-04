package com.example.playlistmaker.settings.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.media.ui.models.BindingFragment
import com.example.playlistmaker.settings.ui.viewModels.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {
    private val viewModel by viewModel<SettingsViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getThemeLiveData().observe(viewLifecycleOwner) { isDarkTheme ->
            binding.themeSwitch.isChecked = isDarkTheme
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }


        binding.back.setNavigationOnClickListener {
            val fragment = parentFragmentManager.findFragmentById(R.id.root_fragment)

            if (fragment != null) {
                parentFragmentManager.beginTransaction()
                    .remove(fragment).commit()
            }
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

        binding.btnAgreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.agreement_link)))

            startActivity(intent)
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}