package ru.androidschool.intensiv.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.androidschool.intensiv.data.local.entities.ActorDbEntity
import ru.androidschool.intensiv.data.local.entities.MovieActorJoin
import ru.androidschool.intensiv.data.local.entities.MovieDbEntity

data class MovieWithActors(
    @Embedded
    val movie: MovieDbEntity,
    @Relation(
        parentColumn = "movieId",
        entity = ActorDbEntity::class,
        entityColumn = "actorId",
        associateBy = Junction(
            value = MovieActorJoin::class,
            parentColumn = "movieId",
            entityColumn = "actorId"
        )
    )
    val actors: List<ActorDbEntity>
)
