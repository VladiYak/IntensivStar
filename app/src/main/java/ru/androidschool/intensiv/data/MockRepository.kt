package ru.androidschool.intensiv.data

object MockRepository {

    fun getMovies(): List<Movie> {

        val moviesList = mutableListOf<Movie>()
        for (x in 0..10) {
            val movie = Movie(
                title = "Spider-Man $x",
                voteAverage = 10.0 - x
            )
            moviesList.add(movie)
        }

        return moviesList
    }

    fun getTvShow(): List<TvShow> {
        val tvShowList = mutableListOf<TvShow>()
        for (x in 0..10) {
            val tvShow = TvShow(
                title = "Sherlock",
                voteAverage = 10.0 - x,
                image = "https://avatars.mds.yandex.net/get-kinopoisk-image/1629390/bab5549f-1f96-4a0e-8762-7fac21a07cab/1920x"
            )
            tvShowList.add(tvShow)
        }

        return tvShowList
    }
}
