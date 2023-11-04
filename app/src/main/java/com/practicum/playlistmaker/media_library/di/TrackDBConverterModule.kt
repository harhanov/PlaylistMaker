package com.practicum.playlistmaker.media_library.di

import com.practicum.playlistmaker.media_library.data.converters.TrackDBConverter
import org.koin.dsl.module

val trackDBConverterModule = module {
    single { TrackDBConverter() }
}