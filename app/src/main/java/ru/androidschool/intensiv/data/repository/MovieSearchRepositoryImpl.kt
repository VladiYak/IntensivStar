package ru.androidschool.intensiv.data.repository

import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.data.network.dto.Movies
import ru.androidschool.intensiv.domain.repository.MovieSearchRepository

class MovieSearchRepositoryImpl: MovieSearchRepository {
    override fun searchMovies(searchString: String, language: String): Observable<Movies> {
        return MovieApiClient.apiClient.searchMovie(language, searchString)
    }
}