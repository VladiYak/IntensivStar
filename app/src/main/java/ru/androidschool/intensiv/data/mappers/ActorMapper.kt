package ru.androidschool.intensiv.data.mappers

import ru.androidschool.intensiv.data.network.dto.Cast
import ru.androidschool.intensiv.domain.entity.ActorInfoEntity

class ActorMapper: BaseMapper<Cast, ActorInfoEntity> {
    override fun mapTo(from: Cast): ActorInfoEntity {
        return ActorInfoEntity(
            id = from.id ?: 0,
            imageUrl = from.profilePath.orEmpty(),
            fullName = from.originalName.orEmpty()
        )
    }
}