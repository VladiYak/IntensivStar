package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.Actors
import ru.androidschool.intensiv.data.dto.MovieDetails
import ru.androidschool.intensiv.databinding.MovieDetailsFragmentBinding
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.BaseFragment
import ru.androidschool.intensiv.ui.feed.FeedFragment
import ru.androidschool.intensiv.utils.applyIoMainSchedulers
import ru.androidschool.intensiv.utils.loadImage
import timber.log.Timber

class MovieDetailsFragment : BaseFragment() {

    private var _binding: MovieDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieDetailsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBack.setOnClickListener { onBackPressed() }

        val id = getMovieId()

        fetchMovieDetails(id)

        fetchMovieActors(id)
    }

    private fun fetchMovieActors(id: Int) {
        val disposable = MovieApiClient.apiClient.getMovieActorsById(id)
            .map { actors ->
                actors.cast.map { ActorItem(it) }
            }
            .applyIoMainSchedulers()
            .subscribe({ actorList ->
                binding.actorsRecyclerView.adapter = adapter.apply { addAll(actorList) }
            }, { throwable ->
                Timber.e(throwable)
            })

        compositeDisposable.add(disposable)
    }

    private fun fetchMovieDetails(id: Int) {
        val disposable = MovieApiClient.apiClient.getMovieDetailsById(id)
            .applyIoMainSchedulers()
            .subscribe({ movie ->
                with(binding) {
                    movieTitle.text = movie.title
                    movieRating.rating = movie.voteAverage?.toFloat() ?: 0.0f
                    movieDescription.text = movie.overview
                    movieStudio.text =
                        movie.productionCompanies?.joinToString { it.name.toString() }
                    movieGenre.text = movie.genres?.joinToString { it.id.toString() }
                    movieYear.text = movie.releaseDate

                    movieImage.loadImage(movie.posterPath)
                }
            }, { throwable ->
                Timber.e(throwable)
            })

        compositeDisposable.add(disposable)
    }

    private fun getMovieId() = requireArguments().getInt(FeedFragment.KEY_MOVIE_ID)

    private fun onBackPressed() {
        requireActivity().onBackPressed()
    }
}
