package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.example.playlistmaker.media.ui.adapters.PagerAdapter
import com.example.playlistmaker.media.ui.models.BindingFragment
import com.example.playlistmaker.search.ui.fragments.SearchFragment
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : BindingFragment<FragmentMediaBinding>() {
    private lateinit var tabMediator: TabLayoutMediator

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMediaBinding {
        return FragmentMediaBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pager.adapter = PagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle
        )

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Избранные треки"
                1 -> tab.text = "Плейлисты"
            }
        }
        tabMediator.attach()

        binding.back.setNavigationOnClickListener {
            val fragment = parentFragmentManager.findFragmentById(R.id.root_fragment)

            if (fragment != null) {
                parentFragmentManager.beginTransaction()
                    .remove(fragment).commit()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        tabMediator.detach()
    }

    companion object {
        fun newInstance() = MediaFragment()
    }
}