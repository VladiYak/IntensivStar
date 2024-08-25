package ru.androidschool.intensiv.ui.feed

import androidx.annotation.StringRes
import ru.androidschool.intensiv.data.dto.MovieDto

data class FeedItem(
    @StringRes val title: Int,
    val items: List<MovieDto>?
)
