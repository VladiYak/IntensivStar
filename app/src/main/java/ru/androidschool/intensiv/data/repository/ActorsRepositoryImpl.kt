package ru.androidschool.intensiv.data.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.data.network.dto.Actors
import ru.androidschool.intensiv.data.network.dto.MovieDetails
import ru.androidschool.intensiv.domain.repository.ActorsRepository
import timber.log.Timber

class ActorsRepositoryImpl: ActorsRepository {
    override fun getActorsById(id: Int): Single<Actors> {
        return MovieApiClient.apiClient.getMovieActorsById(id)
            .onErrorReturn {
                Timber.d(it)
                Actors()
            }

    }
}