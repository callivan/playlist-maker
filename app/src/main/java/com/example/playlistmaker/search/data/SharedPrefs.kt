package com.example.playlistmaker.search.data

interface SharedPrefs<ReturnType, Type> {
    fun get(): ReturnType
    fun add(props: Type): Unit
    fun clean(): Unit
}