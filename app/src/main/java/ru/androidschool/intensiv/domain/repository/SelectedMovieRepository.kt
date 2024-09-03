package ru.androidschool.intensiv.domain.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.data.local.MovieWithActors
import ru.androidschool.intensiv.data.local.entities.ActorDbEntity
import ru.androidschool.intensiv.data.local.entities.MovieDbEntity

interface SelectedMovieRepository {

    fun saveToDb(
        currentDetailsForDb: MovieDbEntity,
        currentActors: List<ActorDbEntity>
    ): Completable

    fun getSelectedMovies(): Observable<List<MovieWithActors>>
    fun checkSavedMovieById(movieId: Int): Single<Boolean>
    fun deleteMovieById(movieId: Int): Completable
}