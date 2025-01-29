package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.ITunesAPIRequest
import com.example.playlistmaker.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val BASE_URL = "https://itunes.apple.com"

class RetrofitNetworkClient : NetworkClient {
    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()


    private val iTunesAPI = retrofit.create<ITunesAPIService>()

    override fun request(dto: Any): Response {
        if (dto is ITunesAPIRequest) {
            val res = iTunesAPI.getTracks(dto.text).execute()
            val body = res.body() ?: Response()

            return body.apply { resultCode = res.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}