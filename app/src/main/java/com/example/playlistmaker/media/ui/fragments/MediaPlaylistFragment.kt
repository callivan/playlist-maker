package com.example.playlistmaker.media.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentMediaPlaylistBinding
import com.example.playlistmaker.media.ui.models.BindingFragment

class MediaPlaylistFragment : BindingFragment<FragmentMediaPlaylistBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMediaPlaylistBinding {
        return FragmentMediaPlaylistBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance() = MediaPlaylistFragment()
    }
}