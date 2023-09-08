package com.practicum.playlistmaker.creator

import android.content.Context
import com.practicum.playlistmaker.player.PlayerRepository
import com.practicum.playlistmaker.player.data.MediaPlayerControl
import com.practicum.playlistmaker.player.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.local.LocalDataSourceImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.model.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.settings.data.SettingsLocalDataSource
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {
    private const val LOCAL_STORAGE = "local_storage"

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient(context),
            LocalDataSourceImpl(context.getSharedPreferences(LOCAL_STORAGE, Context.MODE_PRIVATE))
        )
    }

   fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    fun providePlayerInteractor(trackForPlayer: Track): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository(trackForPlayer))
    }
    private fun getPlayerRepository(trackForPlayer: Track): PlayerRepository {
        return PlayerRepositoryImpl(MediaPlayerControl(trackForPlayer))
    }

    fun createSettingsRepository(dataSource: SettingsLocalDataSource): SettingsRepository {
        return SettingsRepositoryImpl(dataSource)
    }

    fun createSharingInteractor(externalNavigator: ExternalNavigator): SharingInteractor {
        return SharingInteractorImpl(externalNavigator)
    }

}