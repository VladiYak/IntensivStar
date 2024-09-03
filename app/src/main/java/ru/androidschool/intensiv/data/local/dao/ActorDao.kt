package ru.androidschool.intensiv.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import io.reactivex.Completable
import ru.androidschool.intensiv.data.local.entities.ActorDbEntity

@Dao
interface ActorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveActor(actor: ActorDbEntity): Completable

    @Insert
    fun saveActors(actors: List<ActorDbEntity>): Completable

}