package com.example.playlistmaker.iTunesAPIService

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesAPI {
    @GET("/search?entity=song")
    fun getTracks(@Query("term") text: String): Call<iTunesAPIResponse>
}