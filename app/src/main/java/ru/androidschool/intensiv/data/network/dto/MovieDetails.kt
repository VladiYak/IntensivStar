package ru.androidschool.intensiv.data.network.dto

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class MovieDetails(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("imdb_id")
    val imdbId: String? = "",
    @SerializedName(value = "title")
    val title: String? = "",
    @SerializedName("original_title")
    val originalTitle: String? = "",
    @SerializedName("original_language")
    val originalLanguage: String? = "",
    @SerializedName("original_country")
    val originalCountry: List<String>? = listOf(),
    @SerializedName("overview")
    val overview: String? = "",
    @SerializedName(value = "release_date")
    val releaseDate: String? = "",
    @SerializedName("popularity")
    val popularity: Double? = 0.0,
    @SerializedName("vote_average")
    val voteAverage: Double? = 0.0,
    @SerializedName("vote_count")
    val voteCount: Int? = 0,
    @SerializedName("adult")
    val adult: Boolean? = false,
    @SerializedName("backdrop_path")
    val backdropPath: String? = "",
    @SerializedName("video")
    val video: Boolean? = false,
    @SerializedName("genre_ids")
    val genres: List<Genre>? = listOf(),
    @SerializedName("runtime")
    val runtime: Int? = 0,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>? = listOf(),
    @SerializedName("production_country")
    val productionCountry: List<ProductionCountry>? = listOf(),
    @SerializedName("spoken_language")
    val spokenLanguage: List<SpokenLanguage>? = listOf(),
    @SerializedName("homepage")
    val homepage: String? = "",
    @SerializedName("status")
    val status: String? = ""
) {
    @SerializedName("poster_path")
    var posterPath: String? = null
        get() = "${BuildConfig.IMAGE_URL}$field"

}