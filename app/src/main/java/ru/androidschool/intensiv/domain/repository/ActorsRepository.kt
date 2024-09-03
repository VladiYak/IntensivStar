package ru.androidschool.intensiv.domain.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.network.dto.Actors

interface ActorsRepository {
    fun getActorsById(id: Int): Single<Actors>
}