package ru.androidschool.intensiv.data.network.dto

import com.google.gson.annotations.SerializedName

data class TvShows(
    @SerializedName("page")
    val page: Int? = 0,
    @SerializedName("results")
    val results: List<TvShowDto>? = listOf(),
    @SerializedName("total_pages")
    val totalPages: Int? = 0,
    @SerializedName("total_results")
    val totalResults: Int? = 0
)