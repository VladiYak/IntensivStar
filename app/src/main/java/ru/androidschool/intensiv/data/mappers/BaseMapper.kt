package ru.androidschool.intensiv.data.mappers

interface BaseMapper<FROM, TO> {
    fun mapTo(from: FROM): TO
}