package ru.androidschool.intensiv.utils

import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.data.dto.Cast
import ru.androidschool.intensiv.data.dto.MovieDetails
import ru.androidschool.intensiv.local.entities.ActorEntity
import ru.androidschool.intensiv.local.entities.MovieEntity

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

fun MovieDetails.toEntity(): MovieEntity {
    return MovieEntity(
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

fun List<Cast>.toActorEntityList(): List<ActorEntity> {
    return this.map { cast ->
        ActorEntity(
            actorId = cast.id ?: 0,
            imageUrl = cast.profilePath ?: "",
            fullName = cast.originalName ?: ""
        )
    }
}

