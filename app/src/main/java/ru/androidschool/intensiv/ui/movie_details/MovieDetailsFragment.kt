package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.Actors
import ru.androidschool.intensiv.data.dto.MovieDetails
import ru.androidschool.intensiv.databinding.MovieDetailsFragmentBinding
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.feed.FeedFragment
import ru.androidschool.intensiv.utils.loadImage
import timber.log.Timber

class MovieDetailsFragment : Fragment(R.layout.movie_details_fragment) {

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
        val movieActors = MovieApiClient.apiClient.getMovieActorsById(id ?: 0)
        movieActors.enqueue(object : Callback<Actors> {
            override fun onResponse(call: Call<Actors>, response: Response<Actors>) {
                response.body()?.cast?.let { actors ->
                    val actorList = actors.map { ActorItem(it) }
                    binding.actorsRecyclerView.adapter = adapter.apply { addAll(actorList) }
                }
            }

            override fun onFailure(call: Call<Actors>, t: Throwable) {
                Timber.e(t)
            }

        })
    }

    private fun fetchMovieDetails(id: Int) {
        val movieDetails = MovieApiClient.apiClient.getMovieDetailsById(id)
        movieDetails.enqueue(object : Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                response.body()?.let { movie ->
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
                }
            }

            override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                Timber.e(t)
            }

        })
    }

    private fun getMovieId() = requireArguments().getInt(FeedFragment.KEY_MOVIE_ID)

    private fun onBackPressed() {
        requireActivity().onBackPressed()
    }
}
