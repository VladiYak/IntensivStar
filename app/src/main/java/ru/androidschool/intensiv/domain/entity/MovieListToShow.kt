package ru.androidschool.intensiv.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieListToShow(
    val movies: List<MovieEntity>
): Parcelable
