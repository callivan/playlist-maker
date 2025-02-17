package com.example.playlistmaker.player.domain.useCases

import androidx.activity.ComponentActivity
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.player.domain.models.Track
import com.google.gson.Gson

class GetTrackUseCases() {
    fun getTrackFromIntent(context: ComponentActivity): Track {
        return Gson().fromJson(context.intent.getStringExtra(Const.TRACK), Track::class.java)
    }
}