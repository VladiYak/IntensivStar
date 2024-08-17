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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.MovieDto
import ru.androidschool.intensiv.data.dto.Movies
import ru.androidschool.intensiv.databinding.FeedFragmentBinding
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.afterTextChanged
import timber.log.Timber

class FeedFragment : Fragment(R.layout.feed_fragment) {

    private var _binding: FeedFragmentBinding? = null
    private var _searchBinding: FeedHeaderBinding? = null

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


        searchBinding.searchToolbar.binding.searchEditText.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > MIN_LENGTH) {
                openSearch(it.toString())
            }
        }

        binding.moviesRecyclerView.adapter = adapter


        fetchUpcomingMovies()
        fetchNowPlayingMovies()
        fetchPopularMovies()


    }

    private fun fetchNowPlayingMovies() {
        val getNowPlayingMovies = MovieApiClient.apiClient.getNowPlayingMovies()
        loadAndShowMovies(getNowPlayingMovies, R.string.recommended)
    }

    private fun fetchUpcomingMovies() {
        val getUpcomingMovies = MovieApiClient.apiClient.getUpcomingMovies()
        loadAndShowMovies(getUpcomingMovies, R.string.upcoming)
    }

    private fun fetchPopularMovies() {
        val getPopularMovies = MovieApiClient.apiClient.getPopularMovies()
        loadAndShowMovies(getPopularMovies, R.string.popular)
    }

    private fun loadAndShowMovies(getMovies: Call<Movies>, @StringRes title: Int) {
        getMovies.enqueue(object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                response.body()?.results?.let { results ->
                    val movies = listOf(
                        MainCardContainer(
                            title,
                            results.map {
                                MovieItem(it) { movie ->
                                    openMovieDetails(movie)
                                }
                            }
                        )
                    )
                    adapter.apply { addAll(movies) }
                }
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Timber.e(t)
            }

        })
    }

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
    }

    companion object {
        const val MIN_LENGTH = 3
        const val KEY_TITLE = "title"
        const val KEY_SEARCH = "search"
        const val KEY_MOVIE_ID = "id"
    }
}
