package com.practicum.playlistmaker.search.di

import com.practicum.playlistmaker.search.ui.TrackListAdapter
import org.koin.dsl.module

val searchUiModule = module {
    factory { TrackListAdapter() }
}