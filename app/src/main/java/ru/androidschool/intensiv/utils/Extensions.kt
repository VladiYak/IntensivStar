package ru.androidschool.intensiv.utils

import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import ru.androidschool.intensiv.data.network.dto.Cast
import ru.androidschool.intensiv.data.network.dto.MovieDetails
import ru.androidschool.intensiv.data.local.entities.ActorDbEntity
import ru.androidschool.intensiv.data.local.entities.MovieDbEntity

fun ImageView.loadImage(imageUrl: String?) {
    Picasso.get()
        .load(imageUrl)
        .into(this)
}

fun View.setDebouncedClickListener(
    debounceTime: Long = 1000L,
    action: () -> Unit
) {
    var lastClickTime = 0L

    this.setOnClickListener {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime >= debounceTime) {
            action()
            lastClickTime = currentTime
        }
    }
}

fun MovieDetails.toEntity(): MovieDbEntity {
    return MovieDbEntity(
        movieId = this.id,
        title = this.originalTitle,
        rating = this.voteAverage?.toFloat(),
        movieDescription = this.overview,
        studioName = this.productionCompanies?.joinToString { it.name.toString() },
        genre = this.genres?.joinToString { it.id.toString() },
        year = this.releaseDate,
        posterPath = this.posterPath
    )
}

fun List<Cast>.toActorEntityList(): List<ActorDbEntity> {
    return this.map { cast ->
        ActorDbEntity(
            actorId = cast.id ?: 0,
            imageUrl = cast.profilePath ?: "",
            fullName = cast.originalName ?: ""
        )
    }
}

