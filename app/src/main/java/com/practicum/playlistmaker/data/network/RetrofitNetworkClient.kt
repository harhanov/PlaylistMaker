package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.Response

import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(iTunesAPIService::class.java)

    override fun doSearch(request: TracksSearchRequest): Response {
        val resp = iTunesService.search(request.text).execute()

        return if (resp.isSuccessful) {
            resp.body() ?: Response().apply { resultCode = resp.code() }
        } else {
            Response().apply { resultCode = resp.code() }
        }
    }
}