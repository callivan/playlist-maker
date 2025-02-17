package com.example.playlistmaker.search.domain.models

import com.example.playlistmaker.search.data.dto.Response

data class TracksResponse(val tracks: List<Track>) : Response()
