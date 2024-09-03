package ru.androidschool.intensiv.presentation.feed

import androidx.annotation.StringRes
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.mappers.MovieDtoMapper
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.domain.interactor.FeedInteractor
import ru.androidschool.intensiv.domain.entity.MovieEntity
import ru.androidschool.intensiv.domain.entity.MovieListToShow
import ru.androidschool.intensiv.utils.MovieType
import ru.androidschool.intensiv.utils.applyIoMainSchedulers
import timber.log.Timber

class FeedPresenter(private val feedInteractor: FeedInteractor) {

    private val compositeDisposable = CompositeDisposable()
    private val movieDtoMapper = MovieDtoMapper()

    var view: FeedView? = null

    fun attachView(view: FeedView) {
        this.view = view
        subscribeForSearchInput(view)
    }

    private fun subscribeForSearchInput(view: FeedView) {
        view.searchBarObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { searchString ->
                view.startSearch(searchString)
            }.let {
                compositeDisposable.add(it)
            }
    }

    fun fetchAllMovies(language: String) {
        feedInteractor.getFeedMovie(language)
            .applyIoMainSchedulers()
            .doOnSubscribe {
                view?.showProgress(true)
            }
            .doFinally {
                view?.showProgress(false)
            }
            .subscribe { response ->
                var nowPlayingItems = emptyList<MainCardContainer>()
                var upcomingItems = emptyList<MainCardContainer>()
                var popularItems = emptyList<MainCardContainer>()

                response.map { entry ->
                    when (entry.key) {
                        MovieType.NOW_PLAYING -> {
                            nowPlayingItems = movieListConverter(
                                R.string.recommended,
                                entry.value.results ?: listOf()
                            )
                        }

                        MovieType.POPULAR -> {
                            popularItems = movieListConverter(
                                R.string.popular,
                                entry.value.results ?: listOf()
                            )
                        }

                        MovieType.UPCOMING -> {
                            upcomingItems = movieListConverter(
                                R.string.upcoming,
                                entry.value.results ?: listOf()
                            )
                        }
                    }
                }
                view?.showMovies(nowPlayingItems + upcomingItems + popularItems)
            }
            .let { compositeDisposable.add(it) }
    }

    private fun movieListConverter(@StringRes header: Int, value: List<MovieDto>) =
        listOf(
            MainCardContainer(
                header,
                value.map { movieDto ->
                    movieDtoMapper.mapTo(movieDto)
                }.map {
                    MovieItem(it) { movie ->
                        view?.openMovieDetails(movie)
                    }
                }
            )
        )

    fun startSearch(searchString: String, language: String) {
        feedInteractor.searchMovieByTitle(searchString, language)
            .applyIoMainSchedulers()
            .doOnSubscribe {
                view?.showProgress(true)
            }
            .doFinally {
                view?.showProgress(false)
            }
            .doOnError {
                Timber.d(it)
            }
            .doOnNext { response ->
                val moviesDtoList = response.results ?: listOf()
                val moviesEntitiesList = moviesDtoList.map { movieDto ->
                    movieDtoMapper.mapTo(movieDto)
                }
                view?.showSearchResults(MovieListToShow(moviesEntitiesList), searchString)
            }
            .subscribe()
            .let {
                compositeDisposable.add(it)
            }
    }

    fun detachView(view: FeedView) {
        this.view = null
        compositeDisposable.clear()
    }


    interface FeedView {
        fun showMovies(movies: List<MainCardContainer>)
        fun showProgress(isVisible: Boolean)
        fun openMovieDetails(movieEntity: MovieEntity)
        fun startSearch(initialString: String)
        fun showSearchResults(movieListToShow: MovieListToShow, searchString: String)
        fun searchBarObservable(): Observable<String>
    }

}

