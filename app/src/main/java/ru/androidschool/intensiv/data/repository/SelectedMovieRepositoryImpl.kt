package ru.androidschool.intensiv.data.repository

import android.content.Context
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.data.local.MovieWithActors
import ru.androidschool.intensiv.data.local.MoviesDatabase
import ru.androidschool.intensiv.data.local.dao.ActorDao
import ru.androidschool.intensiv.data.local.dao.MovieDao
import ru.androidschool.intensiv.data.local.dao.MovieWithActorsDao
import ru.androidschool.intensiv.data.local.entities.ActorDbEntity
import ru.androidschool.intensiv.data.local.entities.MovieActorJoin
import ru.androidschool.intensiv.data.local.entities.MovieDbEntity
import ru.androidschool.intensiv.domain.repository.SelectedMovieRepository

class SelectedMovieRepositoryImpl(
    private val context: Context,
    private val movieDao: MovieDao = MoviesDatabase.getDatabase(context).movieDao(),
    private val actorDao: ActorDao = MoviesDatabase.getDatabase(context).actorDao(),
    private val movieWithActorsDao: MovieWithActorsDao = MoviesDatabase.getDatabase(context).movieWithActorsDao()
) : SelectedMovieRepository{
    override fun saveToDb(
        currentDetailsForDb: MovieDbEntity,
        currentActors: List<ActorDbEntity>
    ): Completable {
        val resultOne = movieDao.saveMovie(currentDetailsForDb)
        val resultTwo = actorDao.saveActors(currentActors)
        val resultThree = currentActors.map { actorEntity ->
            MovieActorJoin(
                movieId = currentDetailsForDb.movieId,
                actorId = actorEntity.actorId
            )
        }.let {
            movieWithActorsDao.saveJoins(it)
        }
        return resultOne.andThen(resultTwo).andThen(resultThree)
    }

    override fun getSelectedMovies(): Observable<List<MovieWithActors>> {
        return movieWithActorsDao.getAllSelectedMovies()
    }

    override fun checkSavedMovieById(movieId: Int): Single<Boolean> {
        return movieDao.isExist(movieId)
    }

    override fun deleteMovieById(movieId: Int): Completable {
        return movieDao.deleteMovieById(movieId)
    }


}