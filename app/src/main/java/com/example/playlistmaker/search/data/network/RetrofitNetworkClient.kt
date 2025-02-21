package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.ITunesAPIRequest
import com.example.playlistmaker.search.data.dto.Response

class RetrofitNetworkClient(private val iTunesAPI: ITunesAPIService) : NetworkClient {
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