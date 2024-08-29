package ru.androidschool.intensiv.local

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "actorId"])
data class MovieActorJoin(
    val movieId: Int,
    val actorId: Int
)
