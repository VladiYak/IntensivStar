package ru.androidschool.intensiv.data.local.entities

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "actorId"])
data class MovieActorJoin(
    val movieId: Int,
    val actorId: Int
)
