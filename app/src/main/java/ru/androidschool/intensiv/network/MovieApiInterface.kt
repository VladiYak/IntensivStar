package ru.androidschool.intensiv.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import ru.androidschool.intensiv.data.dto.Actors
import ru.androidschool.intensiv.data.dto.Cast
import ru.androidschool.intensiv.data.dto.MovieDetails
import ru.androidschool.intensiv.data.dto.Movies
import ru.androidschool.intensiv.data.dto.TvShows

interface MovieApiInterface {
    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("language") language: String = "ru"): Call<Movies>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("language") language: String = "ru"): Call<Movies>

    @GET("movie/popular")
    fun getPopularMovies(@Query("language") language: String = "ru"): Call<Movies>

    @GET("tv/popular")
    fun getPopularTvShows(@Query("language") language: String = "ru"): Call<TvShows>

    @GET("movie/{id}")
    fun getMovieDetailsById(
        @Path("id") id: Int,
        @Query("language") language: String = "ru"
    ): Call<MovieDetails>

    @GET("movie/{movie_id}/credits")
    fun getMovieActorsById(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "ru"
    ): Call<Actors>

}
