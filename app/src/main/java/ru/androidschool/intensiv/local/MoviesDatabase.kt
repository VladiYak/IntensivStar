package ru.androidschool.intensiv.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.androidschool.intensiv.local.dao.ActorDao
import ru.androidschool.intensiv.local.dao.MovieDao
import ru.androidschool.intensiv.local.dao.MovieWithActorsDao
import ru.androidschool.intensiv.local.entities.ActorEntity
import ru.androidschool.intensiv.local.entities.MovieEntity

@Database(entities = [MovieEntity::class, ActorEntity::class, MovieActorJoin::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun actorDao(): ActorDao
    abstract fun movieWithActorsDao(): MovieWithActorsDao



    companion object {
        @Volatile
        private var INSTANCE: MoviesDatabase? = null

        fun getDatabase(context: Context): MoviesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoviesDatabase::class.java,
                    "movies_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}