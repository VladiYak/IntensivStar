package ru.androidschool.intensiv.data.network.dto

import com.google.gson.annotations.SerializedName

data class Actors(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("cast")
    val cast: List<Cast> = listOf()
)