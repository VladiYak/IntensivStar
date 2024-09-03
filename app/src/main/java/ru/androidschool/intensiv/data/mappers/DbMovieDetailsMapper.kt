package ru.androidschool.intensiv.data.mappers

import ru.androidschool.intensiv.data.local.entities.MovieDbEntity
import ru.androidschool.intensiv.domain.entity.MovieDetailsEntity

class DbMovieDetailsMapper : BaseMapper<MovieDetailsEntity, MovieDbEntity> {

    override fun mapTo(from: MovieDetailsEntity): MovieDbEntity {
        return MovieDbEntity(
            movieId = from.id,
            title = from.movieName,
            rating = from.movieRating,
            movieDescription = from.movieDescription,
            studioName = from.studioName,
            genre = from.genre,
            year = from.year,
            posterPath = from.posterPath
        )
    }
}