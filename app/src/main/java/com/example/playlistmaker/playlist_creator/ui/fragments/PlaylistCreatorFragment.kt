package com.example.playlistmaker.playlist_creator.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.databinding.FragmentPlaylistCreatorBinding
import com.example.playlistmaker.playlist_creator.ui.models.PlaylistCreatorScreenState
import com.example.playlistmaker.playlist_creator.ui.models.PlaylistUI
import com.example.playlistmaker.playlist_creator.ui.models.TrackUI
import com.example.playlistmaker.playlist_creator.ui.viewModels.PlaylistCreatorViewModel
import com.example.playlistmaker.utils.CustomDrawers
import com.example.playlistmaker.utils.CustomSnackbar
import com.example.playlistmaker.utils.Utils.Companion.dpToPx
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.InputStream
import kotlin.getValue

class PlaylistCreatorFragment() : Fragment() {
    private val viewModel by viewModel<PlaylistCreatorViewModel>()

    private lateinit var binding: FragmentPlaylistCreatorBinding

    private var fileDir: File? = null
    private var inputStream: InputStream? = null

    private var snackbar: Snackbar? = null

    private var showAlertDialog: Boolean = false

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setImage(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track: TrackUI? =
            Gson().fromJson(arguments?.getString(Const.TRACK), TrackUI::class.java)

        val playlist: PlaylistUI? = Gson().fromJson(
            arguments?.getString(Const.PLAYLIST),
            PlaylistUI::class.java
        )

        if (playlist != null) {
            viewModel.setPlaylistData(playlist)

            binding.btnCreate.text = getString(R.string.save)
            binding.back.title = getString(R.string.playlist_update_title)

            binding.editNameText.setText(playlist.name)
            binding.editDescriptionText.setText(playlist.description)
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.modal_end_playlist_creating_title))
            .setMessage(getString(R.string.modal_end_playlist_creating_text))
            .setPositiveButton(getString(R.string.end)) { dialog, _ ->
                findNavController().navigateUp()
                dialog.dismiss()
            }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }.create()

        fun navigateUp() {
            if (showAlertDialog) {
                alertDialog.show()
            } else {
                findNavController().navigateUp()
            }
        }

        viewModel.getPlaylistCreatorLiveData().observe(viewLifecycleOwner) { state ->

            requireActivity().runOnUiThread {
                when (state) {
                    is PlaylistCreatorScreenState.Init -> {

                    }

                    is PlaylistCreatorScreenState.ChangeData -> {
                        showAlertDialog = if (playlist != null) false else
                            (state.playlist.img != null && state.playlist.img!!.isNotEmpty()) || state.playlist.name.isNotEmpty() || (state.playlist.description != null && state.playlist.description!!.isNotEmpty())

                        binding.btnCreate.isEnabled = if (playlist != null)
                            playlist.img != state.playlist.img || playlist.name != state.playlist.name || playlist.description != state.playlist.description else state.playlist.name.isNotEmpty()

                        Glide.with(requireContext())
                            .load(if (playlist != null && playlist.img == state.playlist.img) playlist.img else state.playlist.img?.toUri())
                            .encodeFormat(Bitmap.CompressFormat.WEBP).encodeQuality(70)
                            .override(binding.btnAddImage.width, binding.btnAddImage.height)
                            .centerCrop().placeholder(R.drawable.add_image)
                            .into(binding.btnAddImage)

                        fileDir =
                            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

                        if (playlist?.img != state.playlist.img && state.playlist.img != null) {
                            inputStream =
                                requireContext().contentResolver.openInputStream(state.playlist.img!!.toUri())
                        }
                    }

                    is PlaylistCreatorScreenState.Created -> {
                        snackbar = CustomSnackbar.createSnackbar(
                            requireView(),
                            getString(
                                if (playlist != null) R.string.playlist_updated else R.string.playlist_created,
                                state.playlists.name
                            )
                        )

                        findNavController().navigateUp()

                        snackbar?.show()
                    }
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateUp()
                }
            })

        val dashedBorder = CustomDrawers.Companion.drawCustomDashedBorder(
            strokeWidth = 1f.dpToPx(requireContext()),
            dashLength = 28f.dpToPx(requireContext()),
            dashGap = 28f.dpToPx(requireContext()),
            color = requireContext().getColor(R.color.gray),
            cornerRadius = 8f.dpToPx(requireContext()),
            view = binding.btnAddImage
        )

        binding.btnAddImage.background = dashedBorder

        binding.btnAddImage.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.editNameText.addTextChangedListener(viewModel.nameTextWatcher())
        binding.editDescriptionText.addTextChangedListener(viewModel.descriptionTextWatcher())

        binding.btnCreate.setOnClickListener {
            viewModel.createPlaylist(track = track, filesDir = fileDir, inputStream = inputStream)
        }

        binding.back.setOnClickListener {
            navigateUp()
        }
    }
}