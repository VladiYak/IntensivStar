package ru.androidschool.intensiv.data.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.data.network.dto.Movies
import ru.androidschool.intensiv.domain.repository.MovieRepository
import timber.log.Timber

class UpcomingMovieRepositoryImpl: MovieRepository {
    override fun getMovies(language: String): Single<Movies> {
        return MovieApiClient.apiClient.getUpcomingMovies(language)
            .onErrorReturn {
                Timber.d(it)
                Movies()
            }
    }
}