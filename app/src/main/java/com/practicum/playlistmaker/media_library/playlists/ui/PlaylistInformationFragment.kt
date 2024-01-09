package com.practicum.playlistmaker.media_library.playlists.ui

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistInformationBinding
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel
import com.practicum.playlistmaker.player.domain.TrackModel
import com.practicum.playlistmaker.search.data.model.Track
import com.practicum.playlistmaker.search.ui.TrackListAdapter
import com.practicum.playlistmaker.utils.BottomNavigationUtils
import com.practicum.playlistmaker.utils.DateUtils
import com.practicum.playlistmaker.utils.PlaylistUtils
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistInformationFragment : Fragment(R.layout.playlist_information) {
    private var _binding: PlaylistInformationBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlist: PlaylistModel

    private val viewModel: PlaylistInformationViewModel by viewModel {
        parametersOf(parseIntent())
    }
    private val trackListAdapter: TrackListAdapter by inject()

    private val playlistAdapter: PlaylistAdapter by inject {
        parametersOf(
            requireContext(),
            R.layout.playlist_item_mini,
            R.dimen.mini_cover_corner_radius
        )
    }

    private lateinit var deleteDialog: MaterialAlertDialogBuilder

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = PlaylistInformationBinding.inflate(inflater, container, false)
        BottomNavigationUtils.hideBottomNavigationView(activity)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.totalPlayingTime.observe(viewLifecycleOwner) { totalPlayingTime ->
            totalPlayingTime?.let {
                binding.totalPlayingTime.text = it
            }
        }

        findNavController().addOnDestinationChangedListener { _, _, _ ->
            if (isAdded) {
                if (findNavController().currentDestination?.id == R.id.playlistInformationFragment) {
                    viewModel.refreshPlaylist()
                }
            }
        }

        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            playlist?.let {
                if (viewModel.totalPlayingTime.value == null) {
                    viewModel.calculateTotalPlayingTime()
                }
                updateUI(it)
            }
        }
        viewModel.playlistTracks.observe(viewLifecycleOwner) { tracks ->
            tracks?.let {
                trackListAdapter.setTracks(it.map(TrackModel::trackModelToTrack))
            }
        }

        val bottomSheetContainer = binding.bottomSheetPreferences

        val overlay = binding.overlay

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        viewModel.bottomSheetLiveData.observe(viewLifecycleOwner) { state ->
            bottomSheetBehavior.state = state
        }

        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            playlist?.let {
                if (viewModel.totalPlayingTime.value == null) {
                    viewModel.calculateTotalPlayingTime()
                }
                this.playlist = it
                updateUI(it)
            }
        }

        initListenersAndRecycler()
    }

    private fun initListenersAndRecycler() {

        binding.playlistBackButton.apply {
            setOnClickListener { findNavController().navigateUp() }
        }

        binding.playlistShareButton.apply {
            setOnClickListener {
                checkingPlaylistForEmptinessAndShare()
            }
        }

        binding.share.apply {
            setOnClickListener {
                checkingPlaylistForEmptinessAndShare()
            }
        }



        trackListAdapter.onClickListener = { track ->
            navigateToPlayerFragment(track)
            BottomNavigationUtils.hideBottomNavigationView(activity)
        }

        trackListAdapter.onLongClickListener = { track ->
            showDeleteTrackDialog(track.trackId)
        }

        binding.delete.apply {
            setOnClickListener {
                bottomSheetBehavior.state = STATE_HIDDEN
                playlist.playlistId?.let { it1 -> showDeletePlaylistDialog(it1) }
            }
        }

        binding.trackRecyclerView.apply {
            adapter = trackListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        binding.playlistMenuButton.apply {
            setOnClickListener {
                initPlaylistAdapter()
            }
        }
        binding.edit.apply {
            setOnClickListener {
                openPlaylistEdition()
            }
        }
    }

    private fun checkingPlaylistForEmptinessAndShare() {
        if (viewModel.getIsPlaylistEmpty()) {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.there_are_not_tracks_here), Toast.LENGTH_SHORT
            ).show()
        } else {
            sharePlaylist()
        }
    }

    private fun openPlaylistEdition() {
        val navController = findNavController()
        playlist.playlistId?.let {
            navController.navigate(
                R.id.action_playlistInformationFragment_to_newPlaylistFragment,
                bundleOf(PLAYLIST_ID_KEY to it)
            )
        }
    }

    private fun initPlaylistAdapter() {
        binding.playlistMenuRecyclerView.apply {
            adapter = playlistAdapter
            layoutManager = LinearLayoutManager(this.context)
            playlistAdapter.setPlaylistsList(listOf(playlist))
            bottomSheetBehavior.state = STATE_COLLAPSED
        }
    }

    private fun sharePlaylist() {
        val tracks = viewModel.playlistTracks.value

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val tracksWord =
            playlist.let {
                PlaylistUtils.formatNumberOfTracks(
                    it.numberOfTracks,
                    binding.root.resources
                )
            }

        val playlistDescription = "${playlist.playlistDescription ?: ""}\n$tracksWord"
        val tracksText = tracks?.mapIndexed { index, track ->
            "${index + 1}. ${track.artistName} - ${track.trackName} (${
                track.trackTime?.let {
                    DateUtils.formatTrackTime(it)
                }
            })"
        }?.joinToString("\n")

        val message = "${playlist.playlistName}\n$playlistDescription\n$tracksText"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)

        val chooserIntent = Intent.createChooser(shareIntent, getString(R.string.share_playlist))
        startActivity(chooserIntent)
    }

    private fun showDeleteTrackDialog(trackId: Int) {
        showDeleteDialog(R.string.delete_track, R.string.want_to_delete_track) {
            viewModel.removeTrack(trackId)
        }
    }

    private fun showDeletePlaylistDialog(playlistId: Long) {
        showDeleteDialog(R.string.delete_playlist, R.string.want_to_delete_playlist) {
            viewModel.removePlaylist(playlistId)
            findNavController().popBackStack()
        }
    }

    private fun showDeleteDialog(
        titleRes: Int,
        message: Int,
        positiveButtonAction: () -> Unit,
    ) {
        val dialogBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.dialog_background)
        val titleTextColor = ContextCompat.getColor(requireContext(), R.color.black)
        val dialogTextColor = ContextCompat.getColor(requireContext(), R.color.dorblu)

        val negativeButtonText = createColoredSpannable(R.string.no, dialogTextColor)
        val positiveButtonText = createColoredSpannable(R.string.yes, dialogTextColor)
        val titleText = createColoredSpannable(titleRes, titleTextColor)

        deleteDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(titleText)
            .setMessage(message)
            .setBackground(dialogBackground)
            .setNegativeButton(negativeButtonText) { _, _ -> }
            .setPositiveButton(positiveButtonText) { _, _ -> positiveButtonAction.invoke() }

        deleteDialog.show()
    }

    private fun createColoredSpannable(textRes: Int, colorRes: Int): SpannableString {
        val text = ContextCompat.getString(requireContext(), textRes)
        val spannable = SpannableString(text)
        spannable.setSpan(
            ForegroundColorSpan(colorRes),
            0,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    private fun navigateToPlayerFragment(track: Track) {
        val action =
            PlaylistInformationFragmentDirections.actionPlaylistInformationFragmentToPlayerFragment(
                track
            )
        findNavController().navigate(action)
    }

    private fun updateUI(playlist: PlaylistModel) {
        if (playlist.numberOfTracks > 0) {
            binding.playlistTitle.text = playlist.playlistName
            binding.playlistDescription.text = playlist.playlistDescription
            binding.numberOfTracks.text =
                PlaylistUtils.formatNumberOfTracks(playlist.numberOfTracks, binding.root.resources)
            binding.totalPlayingTime.text = viewModel.totalPlayingTime.value
            Glide.with(binding.playlistCover)
                .load(playlist.playlistImagePath)
                .placeholder(R.drawable.playlist_placeholder)
                .into(binding.playlistCover)
            val tracks =
                viewModel.playlistTracks.value?.map { it.trackModelToTrack() } ?: emptyList()
            trackListAdapter.setTracks(tracks)
            binding.playlistInfoPh.isVisible = false
        } else {
            binding.playlistInfoPh.isVisible = true
        }
    }


    private fun parseIntent(): Long {
        return requireArguments().getLong(PLAYLIST_ID_KEY, 0L)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val PLAYLIST_ID_KEY = "playlistId"
    }
}