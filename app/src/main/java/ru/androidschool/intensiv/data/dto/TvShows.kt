package ru.androidschool.intensiv.data.dto

import com.google.gson.annotations.SerializedName

data class TvShows(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: List<TvShowDto>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)