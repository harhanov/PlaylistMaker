package com.practicum.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.practicum.playlistmaker.search.data.Response
import com.practicum.playlistmaker.search.data.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(iTunesAPIService::class.java)

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun doSearch(request: Any): Response {
        if (!isConnected()) {
            return Response()
                .apply { resultCode = NO_INTERNET_CONNECTION_CODE }
        }
        return if (request is TracksSearchRequest) {
            return withContext(Dispatchers.IO) {
                try {
                    val resp = iTunesService.search(request.text)
                    resp.apply { resultCode = SUCCESS_CODE }
                } catch (e: Throwable) {
                    Response().apply { resultCode = SERVER_ERROR_CODE }
                }
            }
        } else {
            Response().apply { resultCode = BAD_REQUEST_CODE }
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