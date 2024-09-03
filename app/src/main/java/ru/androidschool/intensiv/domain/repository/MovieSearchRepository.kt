package ru.androidschool.intensiv.domain.repository

import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.data.network.dto.Movies

interface MovieSearchRepository {
    fun searchMovies(searchString: String, language: String): Observable<Movies>
}