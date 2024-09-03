package ru.androidschool.intensiv.data.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.data.network.dto.MovieDetails
import ru.androidschool.intensiv.data.network.dto.Movies
import ru.androidschool.intensiv.domain.repository.MovieDetailsRepository
import timber.log.Timber

class MovieDetailsRepositoryImpl: MovieDetailsRepository {
    override fun getMovieDetails(id: Int): Single<MovieDetails> {
        return MovieApiClient.apiClient.getMovieDetailsById(id)
            .onErrorReturn {
                Timber.d(it)
                MovieDetails()
            }
    }
}