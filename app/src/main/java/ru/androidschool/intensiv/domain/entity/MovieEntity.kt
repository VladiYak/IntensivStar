package ru.androidschool.intensiv.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieEntity(
    val movieId: Int,
    val title: String,
    val rating: Float,
    val posterUrl: String,
    val horizontalPosterUrl: String
) : Parcelable
