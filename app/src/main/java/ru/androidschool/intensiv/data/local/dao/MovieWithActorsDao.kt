package ru.androidschool.intensiv.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Completable
import io.reactivex.Observable
import ru.androidschool.intensiv.data.local.entities.MovieActorJoin
import ru.androidschool.intensiv.data.local.MovieWithActors

@Dao
interface MovieWithActorsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveJoins(joins: List<MovieActorJoin>): Completable

    @Transaction
    @Query("SELECT * FROM moviesManyToMany")
    fun getAllSelectedMovies(): Observable<List<MovieWithActors>>

}