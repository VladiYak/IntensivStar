package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.MovieDto
import ru.androidschool.intensiv.data.dto.Movies
import ru.androidschool.intensiv.databinding.FeedFragmentBinding
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.afterTextChanged
import timber.log.Timber
import java.util.concurrent.TimeUnit

class FeedFragment : Fragment(R.layout.feed_fragment) {

    private var _binding: FeedFragmentBinding? = null
    private var _searchBinding: FeedHeaderBinding? = null

    private val disposables = CompositeDisposable()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchBinding get() = _searchBinding!!

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FeedFragmentBinding.inflate(inflater, container, false)
        _searchBinding = FeedHeaderBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchMovie()

        binding.moviesRecyclerView.adapter = adapter


        fetchUpcomingMovies()
        fetchNowPlayingMovies()
        fetchPopularMovies()

    }

    private fun searchMovie() {
        val disposable = Observable.create<String> { emitter ->
            searchBinding.searchToolbar.binding.searchEditText.afterTextChanged {
                emitter.onNext("$it".trim())
            }
        }.debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .filter {
                it.length > MIN_LENGTH
            }
            .subscribe {
                openSearch(it)
            }

        disposables.add(disposable)
    }

    private fun fetchNowPlayingMovies() {
        val nowPlayingMovies = MovieApiClient.apiClient.getNowPlayingMovies()
        loadAndShowMovies(nowPlayingMovies, R.string.recommended)
    }

    private fun fetchUpcomingMovies() {
        val upcomingMovies = MovieApiClient.apiClient.getUpcomingMovies()
        loadAndShowMovies(upcomingMovies, R.string.upcoming)
    }

    private fun fetchPopularMovies() {
        val popularMovies = MovieApiClient.apiClient.getPopularMovies()
        loadAndShowMovies(popularMovies, R.string.popular)
    }

    private fun loadAndShowMovies(movies: Single<Movies>, @StringRes title: Int) {
        val disposable = movies
            .subscribeOn(Schedulers.io())
            .map {
                mapToCardContainer(title, it.results)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                adapter.apply {
                    addAll(it)
                }
            }, { throwable ->
                Timber.e(throwable)
            })

        disposables.add(disposable)
    }

    private fun mapToCardContainer(
        @StringRes title: Int,
        movies: List<MovieDto>?
    ): List<MainCardContainer> = listOf(
        MainCardContainer(
            title,
            movies?.map {
                MovieItem(it) { movie ->
                    openMovieDetails(movie)
                }
            } ?: listOf()
        )
    )

    private fun openMovieDetails(movie: MovieDto) {
        val bundle = Bundle()
        bundle.putInt(KEY_MOVIE_ID, movie.id)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    private fun openSearch(searchText: String) {
        val bundle = Bundle()
        bundle.putString(KEY_SEARCH, searchText)
        findNavController().navigate(R.id.search_dest, bundle, options)
    }

    override fun onStop() {
        super.onStop()
        searchBinding.searchToolbar.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _searchBinding = null
        disposables.clear()
    }

    companion object {
        const val MIN_LENGTH = 3
        const val KEY_TITLE = "title"
        const val KEY_SEARCH = "search"
        const val KEY_MOVIE_ID = "id"
    }
}
