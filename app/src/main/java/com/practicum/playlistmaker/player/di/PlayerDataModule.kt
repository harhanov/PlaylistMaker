package com.practicum.playlistmaker.player.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.data.MediaPlayerControl
import com.practicum.playlistmaker.player.data.PlayerControl
import org.koin.dsl.module

val playerDataModule = module {
    factory<PlayerControl> { MediaPlayerControl(mediaPlayer = get()) }

    factory {
        MediaPlayer()
    }
}