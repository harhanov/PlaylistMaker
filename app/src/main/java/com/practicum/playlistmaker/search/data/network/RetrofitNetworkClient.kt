package com.practicum.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.practicum.playlistmaker.ITUNES_BASE_URL
import com.practicum.playlistmaker.NO_INTERNET_CONNECTION_CODE
import com.practicum.playlistmaker.SUCCESS_CODE
import com.practicum.playlistmaker.search.data.Response
import com.practicum.playlistmaker.search.data.TracksSearchRequest
import com.practicum.playlistmaker.search.data.iTunesAPIService


import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(iTunesAPIService::class.java)



    @RequiresApi(Build.VERSION_CODES.M)
    override fun doSearch(request: TracksSearchRequest): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = NO_INTERNET_CONNECTION_CODE }
        }

        val resp = iTunesService.search(request.text).execute()

        return if (resp.isSuccessful) {
            resp.body()?.let {
                Response().apply { resultCode = SUCCESS_CODE }
            } ?: Response().apply { resultCode = resp.code() }
        } else {
            Response().apply { resultCode = resp.code() }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}