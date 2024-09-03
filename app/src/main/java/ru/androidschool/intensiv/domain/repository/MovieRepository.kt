package ru.androidschool.intensiv.domain.repository

import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.data.local.MovieWithActors
import ru.androidschool.intensiv.data.network.dto.Movies

interface MovieRepository {
    fun getMovies(language: String): Single<Movies>

}