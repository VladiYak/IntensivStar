package ru.androidschool.intensiv.data

data class TvShow(
    var title: String = "",
    var voteAverage: Double = 0.0,
    var image: String = ""
) {
    val rating: Float
        get() = voteAverage.div(DIVIDER).toFloat()

    companion object {
        const val DIVIDER = 2
    }

}
