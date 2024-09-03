package ru.androidschool.intensiv.domain.interactor

import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.data.network.dto.Movies
import ru.androidschool.intensiv.data.repository.MovieSearchRepositoryImpl
import ru.androidschool.intensiv.data.repository.NowPlayingMovieRepositoryImpl
import ru.androidschool.intensiv.data.repository.PopularMoviesRepositoryImpl
import ru.androidschool.intensiv.data.repository.UpcomingMovieRepositoryImpl
import ru.androidschool.intensiv.utils.MovieType

class FeedInteractor(
    private val nowPlayingMovieRepository: NowPlayingMovieRepositoryImpl,
    private val upcomingMovieRepository: UpcomingMovieRepositoryImpl,
    private val popularMovieRepository: PopularMoviesRepositoryImpl,
    private val searchRepository: MovieSearchRepositoryImpl
) {

    fun getFeedMovie(language: String): Single<Map<MovieType, Movies>> {
        val getNowPlayingMovies = nowPlayingMovieRepository.getMovies(language)
        val getUpcomingMovies = upcomingMovieRepository.getMovies(language)
        val getPopularMovies = popularMovieRepository.getMovies(language)

        return Single.zip(
            getNowPlayingMovies,
            getUpcomingMovies,
            getPopularMovies
        ) { nowPlayingList: Movies,
            upcomingList: Movies,
            popularList: Movies ->
            mapOf(
                MovieType.NOW_PLAYING to nowPlayingList,
                MovieType.UPCOMING to upcomingList,
                MovieType.POPULAR to popularList
            )
        }
    }

    fun searchMovieByTitle(
        searchString: String,
        language: String
    ): Observable<Movies> {
        return searchRepository.searchMovies(searchString, language)
    }
}