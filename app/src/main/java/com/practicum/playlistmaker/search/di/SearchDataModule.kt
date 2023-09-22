package com.practicum.playlistmaker.search.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.network.ITUNES_BASE_URL
import com.practicum.playlistmaker.search.data.LocalDataSource
import com.practicum.playlistmaker.search.data.network.iTunesAPIService
import com.practicum.playlistmaker.search.data.local.LocalDataSourceImpl
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val localDataSourceModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences(LOCAL_STORAGE, Context.MODE_PRIVATE)
    }

    single { Gson() }

    single<LocalDataSource> {
        LocalDataSourceImpl(sharedPreferences = get())
    }
}

val searchNetworkDataModule = module {
    single { createRetrofit() }
    single { createiTunesAPIService(get()) }
    single<NetworkClient> { RetrofitNetworkClient(androidContext()) }
}

fun createRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun createiTunesAPIService(retrofit: Retrofit): iTunesAPIService {
    return retrofit.create(iTunesAPIService::class.java)
}

private const val LOCAL_STORAGE = "local_storage"