package com.practicum.playlistmaker.media_library.playlists.ui

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.R.color.dorblu
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: NewPlaylistViewModel by viewModel()

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_playlist, container, false)
        _binding = FragmentNewPlaylistBinding.bind(view)

        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(
            com.google.android.material.R.attr.colorOnSecondary,
            typedValue,
            true
        )

        val colorOnSecondary = typedValue.data
        val dorbluColor = ContextCompat.getColor(requireContext(), dorblu)

        binding.playlistDescriptionEdit.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.playlistDescription.setBoxStrokeColorStateList(
                    AppCompatResources.getColorStateList(
                        requireContext(),
                        dorblu
                    )
                )
                binding.playlistDescription.hintTextColor = ColorStateList.valueOf(dorbluColor)
            }
        }
        binding.playlistNameEdit.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.playlistName.setBoxStrokeColorStateList(
                    AppCompatResources.getColorStateList(
                        requireContext(),
                        dorblu
                    )
                )
                binding.playlistName.hintTextColor = ColorStateList.valueOf(dorbluColor)
            }
            viewModel.updateCreateButtonState(text.toString())
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.confirm_dialog_name)
            .setMessage(R.string.data_will_be_lost)
            .setNegativeButton((R.string.cancel)) { _, _ ->
            }
            .setPositiveButton((R.string.complete)) { _, _ ->
                findNavController().popBackStack()
            }
        clickListenersSetUp()
        return view

    }

    private fun clickListenersSetUp() {
        binding.playlistBackButton.setOnClickListener {
            if (checkingForUnsavedData()) {
                confirmDialog.show()
            } else {
                findNavController().navigateUp()
            }
        }
        binding.playlistCover.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.playlistCreateButton.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.onNewPlaylistCreateClick(
                    PlaylistModel(
                        playlistName = binding.playlistNameEdit.text.toString(),
                        playlistDescription = binding.playlistDescriptionEdit.text.toString(),
                        playlistImagePath = selectedImageUri?.toString(),
                    )
                )
            }
        }
    }

    private fun checkingForUnsavedData(): Boolean {
        return selectedImageUri != null || !binding.playlistNameEdit.text.isNullOrEmpty() || !binding.playlistDescriptionEdit.text.isNullOrEmpty()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (checkingForUnsavedData()) {
                    confirmDialog.show()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            state?.render(binding)
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                displaySelectedImage(uri)
            }
        }

    private fun displaySelectedImage(imageUri: Uri?) {
        val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
        val selectedImageBitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
        if (selectedImageBitmap != null) {
            Glide.with(this)
                .load(selectedImageBitmap)
                .transform(
                    CenterCrop(),
                    RoundedCorners(resources.getDimensionPixelSize(R.dimen.big_cover_corner_radius))
                )
                .into(binding.playlistCover)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





