package com.practicum.playlistmaker.player.di

import com.practicum.playlistmaker.player.domain.TrackModel
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {
    viewModel { (track: TrackModel) -> PlayerViewModel(trackModel = track) }
}