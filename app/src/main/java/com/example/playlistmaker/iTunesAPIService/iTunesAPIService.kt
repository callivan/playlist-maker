package com.example.playlistmaker.iTunesAPIService

import com.example.playlistmaker.Status
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ITunesAPIService {
    private val BASE_URL = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val iTunesAPIService = retrofit.create<iTunesAPI>()

    fun getTracks(text: String, cb: (props: iTunesResponse) -> Unit) {

        if (text.isEmpty()) {
            cb(iTunesResponse(Status.INITED, listOf()))
        } else {
            iTunesAPIService.getTracks(text).enqueue(object : Callback<iTunesAPIResponse> {
                override fun onResponse(
                    call: Call<iTunesAPIResponse>, response: Response<iTunesAPIResponse>
                ) {

                    cb(iTunesResponse(Status.PENDING, listOf()))

                    if (text.isNotEmpty() && response.isSuccessful) {
                        val tracks = response.body()?.results.orEmpty()

                        cb(
                            iTunesResponse(
                                if (tracks.isEmpty()) Status.EMPTY else Status.SUCCESS,
                                tracks
                            )
                        )

                    } else {
                        cb(iTunesResponse(Status.ERROR, listOf()))
                    }
                }

                override fun onFailure(call: Call<iTunesAPIResponse>, t: Throwable) {
                    t.printStackTrace()
                    cb(iTunesResponse(Status.ERROR, listOf()))
                }
            })
        }
    }

    fun clean() {

    }
}