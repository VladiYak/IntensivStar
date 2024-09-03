package ru.androidschool.intensiv.data.mappers

import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.domain.entity.MovieEntity

class MovieDtoMapper: BaseMapper<MovieDto, MovieEntity> {
    override fun mapTo(from: MovieDto): MovieEntity {
        return MovieEntity(
            movieId = from.id,
            title = from.title.orEmpty(),
            rating = from.voteAverage?.div(2) ?: 0F,
            posterUrl = from.posterPath.orEmpty(),
            horizontalPosterUrl = from.backdropPath.orEmpty()
        )
    }
}