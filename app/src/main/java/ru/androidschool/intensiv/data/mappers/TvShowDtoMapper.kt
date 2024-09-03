package ru.androidschool.intensiv.data.mappers

import ru.androidschool.intensiv.data.network.dto.TvShowDto
import ru.androidschool.intensiv.domain.entity.TvShowEntity

class TvShowDtoMapper : BaseMapper<TvShowDto, TvShowEntity> {
    override fun mapTo(from: TvShowDto): TvShowEntity {
        return TvShowEntity(
            tvShowId = from.id ?: 0,
            title = from.name.orEmpty(),
            rating = from.voteAverage?.toFloat() ?: 0F,
            horizontalPosterUrl = from.backdropPath.orEmpty()
        )
    }
}