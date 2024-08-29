package ru.androidschool.intensiv.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import ru.androidschool.intensiv.local.entities.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovie(movie: MovieEntity): Completable

    @Delete
    fun deleteMovie(movie: MovieEntity): Completable

    @Query("SELECT EXISTS (SELECT 1 FROM moviesManyToMany WHERE movieId = :movieId)")
    fun isExist(movieId: Int): Single<Boolean>

    @Query("DELETE FROM moviesManyToMany WHERE movieId=:movieId")
    fun deleteMovieById(movieId: Int): Completable

    @Query("SELECT * FROM moviesManyToMany WHERE movieId=:movieId")
    fun getMovieById(movieId: Int?): Single<MovieEntity?>


}