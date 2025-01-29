package com.example.playlistmaker.data

interface ISharedPreferences<ReturnType, Type> {
    fun get(): ReturnType
    fun add(props: Type): Unit
    fun clean(): Unit
}