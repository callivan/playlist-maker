package com.example.playlistmaker.media.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentMediaFavoriteBinding
import com.example.playlistmaker.media.ui.models.BindingFragment

class MediaFavoriteFragment : BindingFragment<FragmentMediaFavoriteBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMediaFavoriteBinding {
        return FragmentMediaFavoriteBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance() = MediaFavoriteFragment()
    }
}