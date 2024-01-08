package com.practicum.playlistmaker.media_library.playlists.ui

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    val binding
        get() = _binding!!

    private val viewModel: NewPlaylistViewModel by viewModel { parametersOf(requireContext()) }

    private val playlistId: Long? by lazy {
        arguments?.getLong(PlaylistInformationFragment.PLAYLIST_ID_KEY)
    }

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        playlistId?.let {
            viewModel.setPlaylistInfo(it)
        }

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
        activity?.window?.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view

    }

    private fun clickListenersSetUp() {
        binding.playlistBackButton.setOnClickListener {
            if (checkingForUnsavedData() && viewModel.isEditMode.value != true) {
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
                val playlistName = binding.playlistNameEdit.text.toString()
                val playlistDescription = binding.playlistDescriptionEdit.text.toString()

                // Сохраняем изображение во внутреннее хранилище
                val playlistImageUri = selectedImageUri?.let {
                    val selectedImageBitmap = BitmapFactory.decodeStream(
                        requireContext().contentResolver.openInputStream(it)
                    )
                    saveImageToInternalStorage(selectedImageBitmap)
                }

                if (viewModel.isEditMode.value == true) {
                    // Редактирование существующего плейлиста
                    viewModel.updatePlaylist(
                        PlaylistModel(
                            playlistId = viewModel.playlistInfo.value?.playlistId ?: 0,
                            playlistName = playlistName,
                            playlistDescription = playlistDescription,
                            playlistImagePath = playlistImageUri?.toString(),
                        )
                    )
                } else {
                    // Создание нового плейлиста
                    viewModel.onNewPlaylistCreateClick(
                        PlaylistModel(
                            playlistName = playlistName,
                            playlistDescription = playlistDescription,
                            playlistImagePath = playlistImageUri?.toString(),
                        )
                    )
                }
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
                if (checkingForUnsavedData() && viewModel.isEditMode.value != true) {
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
        viewModel.playlistInfo.observe(viewLifecycleOwner) { playlistModel ->
            playlistModel?.let {
                Log.d("Debug", "Received updated playlist info: $playlistModel")
                binding.playlistNameEdit.setText(it.playlistName)
                binding.playlistDescriptionEdit.setText(it.playlistDescription)

                it.playlistImagePath?.let { imagePath ->
                    val imageUri = Uri.parse(imagePath)
                    displaySelectedImage(imageUri)
                }
            }
        }
        viewModel.isEditMode.observe(viewLifecycleOwner) { isEditMode ->
            if (isEditMode) {
                binding.playlistCreateButton.text = getString(R.string.save)
                binding.playlistHeader.text = getString(R.string.edit_information)
            }
        }
        viewModel.onBackPressed.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
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
        try {
            val contentResolver = requireContext().contentResolver
            val parcelFileDescriptor = contentResolver.openFileDescriptor(imageUri!!, "r")
            val selectedImageBitmap: Bitmap? =
                BitmapFactory.decodeFileDescriptor(parcelFileDescriptor?.fileDescriptor)
            if (selectedImageBitmap != null) {
                Glide.with(this)
                    .load(selectedImageBitmap)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(resources.getDimensionPixelSize(R.dimen.big_cover_corner_radius))
                    )
                    .into(binding.playlistCover)
            }

            parcelFileDescriptor?.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri? {
        // Генерируем уникальное имя файла, добавляя к базовому имени метку времени
        val timestamp = System.currentTimeMillis()
        val uniqueFileName = "playlist_cover_$timestamp.jpg"

        val file = File(requireContext().filesDir, uniqueFileName)

        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
            Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

