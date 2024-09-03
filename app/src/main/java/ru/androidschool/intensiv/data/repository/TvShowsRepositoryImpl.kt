package ru.androidschool.intensiv.data.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.data.network.dto.Movies
import ru.androidschool.intensiv.data.network.dto.TvShows
import ru.androidschool.intensiv.domain.repository.TvShowsRepository
import timber.log.Timber

class TvShowsRepositoryImpl: TvShowsRepository {
    override fun getPopularTvShows(): Single<TvShows> {
        return MovieApiClient.apiClient.getPopularTvShows()
            .onErrorReturn {
                Timber.d(it)
                TvShows()
            }
    }
}