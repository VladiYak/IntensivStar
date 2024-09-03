package ru.androidschool.intensiv.domain.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.network.dto.MovieDetails

interface MovieDetailsRepository {
    fun getMovieDetails(id: Int): Single<MovieDetails>
}