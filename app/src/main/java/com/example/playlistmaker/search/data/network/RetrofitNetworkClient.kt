package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.ITunesAPIRequest
import com.example.playlistmaker.search.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitNetworkClient : NetworkClient {
    private val retrofit = Retrofit.Builder().baseUrl(Const.ITUNES_API_BASE_URL)
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