package com.example.playlistmaker.domain.models

import com.example.playlistmaker.data.dto.Response

data class TracksResponse(val tracks: List<Track>) : Response()
