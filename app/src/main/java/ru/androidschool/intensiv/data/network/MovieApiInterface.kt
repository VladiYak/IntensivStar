package ru.androidschool.intensiv.data.network

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import ru.androidschool.intensiv.data.network.dto.Actors
import ru.androidschool.intensiv.data.network.dto.Cast
import ru.androidschool.intensiv.data.network.dto.MovieDetails
import ru.androidschool.intensiv.data.network.dto.Movies
import ru.androidschool.intensiv.data.network.dto.TvShows
import java.util.Locale

interface MovieApiInterface {

    private val defaultLanguage: String
        get() = Locale.getDefault().toLanguageTag()

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("language") language: String = defaultLanguage): Single<Movies>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("language") language: String = defaultLanguage): Single<Movies>

    @GET("movie/popular")
    fun getPopularMovies(@Query("language") language: String = defaultLanguage): Single<Movies>

    @GET("tv/popular")
    fun getPopularTvShows(@Query("language") language: String = defaultLanguage): Single<TvShows>

    @GET("movie/{id}")
    fun getMovieDetailsById(
        @Path("id") id: Int,
        @Query("language") language: String = defaultLanguage
    ): Single<MovieDetails>

    @GET("movie/{movie_id}/credits")
    fun getMovieActorsById(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = defaultLanguage
    ): Single<Actors>

    @GET("search/movie")
    fun searchMovie(
        @Query("language") language: String = defaultLanguage,
        @Query("query") query: String
    ): Observable<Movies>

}
