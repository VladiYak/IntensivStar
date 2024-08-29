package ru.androidschool.intensiv.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moviesManyToMany")
data class MovieEntity(
    @PrimaryKey
    val movieId: Int,
    val title: String?,
    val rating: Float?,
    val movieDescription: String?,
    val studioName: String?,
    val genre: String?,
    val year: String?,
    val posterPath: String?,
)
