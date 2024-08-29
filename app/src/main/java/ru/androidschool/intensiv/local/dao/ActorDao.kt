package ru.androidschool.intensiv.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import io.reactivex.Completable
import ru.androidschool.intensiv.local.entities.ActorEntity

@Dao
interface ActorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveActor(actor: ActorEntity): Completable

    @Insert
    fun saveActors(actors: List<ActorEntity>): Completable

}