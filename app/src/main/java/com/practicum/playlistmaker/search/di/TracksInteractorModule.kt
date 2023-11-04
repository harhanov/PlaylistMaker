package com.practicum.playlistmaker.search.di

import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl

import org.koin.dsl.module

val tracksInteractorModule = module {

    single<TracksInteractor> { TracksInteractorImpl(get()) }
}