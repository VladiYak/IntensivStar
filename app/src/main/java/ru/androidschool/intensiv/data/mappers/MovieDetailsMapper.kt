package ru.androidschool.intensiv.data.mappers

import ru.androidschool.intensiv.data.network.dto.MovieDetails
import ru.androidschool.intensiv.domain.entity.MovieDetailsEntity

class MovieDetailsMapper: BaseMapper<MovieDetails, MovieDetailsEntity> {
    override fun mapTo(from: MovieDetails): MovieDetailsEntity {
        return MovieDetailsEntity(
            id = from.id ?: 0,
            movieImageUrl = from.posterPath.orEmpty(),
            movieName = from.title.orEmpty(),
            watchLink = "",
            movieRating = from.voteAverage?.toFloat() ?: 0F,
            movieDescription = from.overview.orEmpty(),
            studioName = from.productionCompanies?.map { company ->
                company.name
            }?.joinToString().orEmpty(),
            genre = from.genres?.map { genre ->
                genre.name
            }?.joinToString()?.replaceFirstChar(Char::titlecase).orEmpty(),
            year = from.releaseDate.orEmpty(),
            posterPath = from.posterPath.orEmpty()
        )
    }
}