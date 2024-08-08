package ru.androidschool.intensiv.data

class TvShow(
    var title: String? = "",
    var voteAverage: Double = 0.0,
    var image: String? = ""
) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}
