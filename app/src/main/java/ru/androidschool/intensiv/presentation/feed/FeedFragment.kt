package ru.androidschool.intensiv.presentation.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Observable
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.databinding.FeedFragmentBinding
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.data.repository.MovieSearchRepositoryImpl
import ru.androidschool.intensiv.data.repository.NowPlayingMovieRepositoryImpl
import ru.androidschool.intensiv.data.repository.PopularMoviesRepositoryImpl
import ru.androidschool.intensiv.data.repository.UpcomingMovieRepositoryImpl
import ru.androidschool.intensiv.domain.interactor.FeedInteractor
import ru.androidschool.intensiv.domain.entity.MovieEntity
import ru.androidschool.intensiv.domain.entity.MovieListToShow
import ru.androidschool.intensiv.presentation.base.BaseFragment
import java.util.Locale

class FeedFragment : BaseFragment(), FeedPresenter.FeedView {

    private var _binding: FeedFragmentBinding? = null
    private var _searchBinding: FeedHeaderBinding? = null

    private lateinit var formSearchEditText: TextView

    private val presenter: FeedPresenter by lazy {
        FeedPresenter(
            FeedInteractor(
                NowPlayingMovieRepositoryImpl(),
                UpcomingMovieRepositoryImpl(),
                PopularMoviesRepositoryImpl(),
                MovieSearchRepositoryImpl()
            )
        )
    }

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
        presenter.attachView(this)
        formSearchEditText = searchBinding.searchToolbar.binding.searchEditText

        presenter.fetchAllMovies(Locale.getDefault().language)

        binding.moviesRecyclerView.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }


    override fun showMovies(movies: List<MainCardContainer>) {
        adapter.apply { addAll(movies) }
    }

    override fun showProgress(isVisible: Boolean) {
        binding.progressBar.progressBar.visibility = if (isVisible) VISIBLE else GONE
    }

    override fun openMovieDetails(movieEntity: MovieEntity) {
        val action =
            FeedFragmentDirections.actionHomeDestToMovieDetailsFragment(movieEntity.movieId)
        findNavController().navigate(action)
    }

    override fun startSearch(initialString: String) {
        presenter.startSearch(initialString, Locale.getDefault().language)
    }

    override fun showSearchResults(movieListToShow: MovieListToShow, searchString: String) {
        if (searchString == formSearchEditText.text.toString()) {
            val searchAction = FeedFragmentDirections
                .actionHomeDestToSearchDest(movieListToShow)
            findNavController().navigate(searchAction)
        } else {
            presenter.startSearch(formSearchEditText.text.toString(), Locale.getDefault().language)
        }
    }

    override fun searchBarObservable(): Observable<String> {
        return searchBinding.searchToolbar.onNewStringObservable()
    }

    override fun onStop() {
        super.onStop()
        searchBinding.searchToolbar.clear()
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
