package ru.androidschool.intensiv.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.androidschool.intensiv.local.entities.ActorEntity
import ru.androidschool.intensiv.local.entities.MovieEntity

data class MovieWithActors(
    @Embedded
    val movie: MovieEntity,
    @Relation(
        parentColumn = "movieId",
        entity = ActorEntity::class,
        entityColumn = "actorId",
        associateBy = Junction(
            value = MovieActorJoin::class,
            parentColumn = "movieId",
            entityColumn = "actorId"
        )
    )
    val actors: List<ActorEntity>
)
