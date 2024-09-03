package ru.androidschool.intensiv.domain.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.network.dto.TvShows

interface TvShowsRepository {
    fun getPopularTvShows(): Single<TvShows>
}