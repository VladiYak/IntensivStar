package ru.androidschool.intensiv.data.dto

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class MovieDetails(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName(value = "title")
    val title: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("original_country")
    val originalCountry: List<String>?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName(value = "release_date")
    val releaseDate: String?,
    @SerializedName("popularity")
    val popularity: Double?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    @SerializedName("adult")
    val adult: Boolean?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("video")
    val video: Boolean?,
    @SerializedName("genre_ids")
    val genres: List<Genre>?,
    @SerializedName("runtime")
    val runtime: Int?,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>?,
    @SerializedName("production_country")
    val productionCountry: List<ProductionCountry>?,
    @SerializedName("spoken_language")
    val spokenLanguage: List<SpokenLanguage>?,
    @SerializedName("homepage")
    val homepage: String?,
    @SerializedName("status")
    val status: String?
) {
    @SerializedName("poster_path")
    var posterPath: String? = null
        get() = "${BuildConfig.IMAGE_URL}$field"
}