package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.media.data.converters.TrackDbConverter
import com.example.playlistmaker.media.data.db.AppDb
import com.example.playlistmaker.search.data.History
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.network.ITunesAPIService
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.sharedPrefs.TracksHistory
import com.example.playlistmaker.theme.data.sharedPrefs.Theme
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val dataModule = module {
    single<com.example.playlistmaker.theme.data.Theme> { Theme(androidContext()) }

    factory<TrackDbConverter> { TrackDbConverter() }

    single<AppDb> {
        Room.databaseBuilder(androidContext(), AppDb::class.java, "database")
            .build()
    }

    single<History> {
        TracksHistory(androidContext())
    }

    single<ITunesAPIService> {
        Retrofit.Builder().baseUrl(Const.ITUNES_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create<ITunesAPIService>()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get<ITunesAPIService>())
    }
}