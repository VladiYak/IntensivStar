package ru.androidschool.intensiv.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actorsManyToMany")
data class ActorEntity(
    @PrimaryKey
    val actorId: Int,
    val imageUrl: String,
    val fullName: String
)
