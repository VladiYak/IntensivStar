package ru.androidschool.intensiv.data.dto

import com.google.gson.annotations.SerializedName

data class Actors(
    @SerializedName("id")
    val id: Int,
    @SerializedName("cast")
    val cast: List<Cast>
)