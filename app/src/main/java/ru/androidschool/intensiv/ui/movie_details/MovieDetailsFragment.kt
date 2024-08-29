package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Completable
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.databinding.MovieDetailsFragmentBinding
import ru.androidschool.intensiv.local.MovieActorJoin
import ru.androidschool.intensiv.local.MoviesDatabase
import ru.androidschool.intensiv.local.dao.ActorDao
import ru.androidschool.intensiv.local.dao.MovieDao
import ru.androidschool.intensiv.local.dao.MovieWithActorsDao
import ru.androidschool.intensiv.local.entities.ActorEntity
import ru.androidschool.intensiv.local.entities.MovieEntity
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.BaseFragment
import ru.androidschool.intensiv.ui.feed.FeedFragment
import ru.androidschool.intensiv.utils.applyIoMainSchedulers
import ru.androidschool.intensiv.utils.loadImage
import ru.androidschool.intensiv.utils.setDebouncedClickListener
import ru.androidschool.intensiv.utils.toActorEntityList
import ru.androidschool.intensiv.utils.toEntity
import timber.log.Timber

class MovieDetailsFragment : BaseFragment() {

    private var _binding: MovieDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private lateinit var moviesDb: MoviesDatabase
    private lateinit var movieDao: MovieDao
    private lateinit var actorDao: ActorDao
    private lateinit var movieWithActorsDao: MovieWithActorsDao

    private var isSelectedMovie = false

    private lateinit var currentDetailsForDb: MovieEntity
    private lateinit var currentActors: List<ActorEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        moviesDb = MoviesDatabase.getDatabase(requireContext())
        movieDao = moviesDb.movieDao()
        actorDao = moviesDb.actorDao()
        movieWithActorsDao = moviesDb.movieWithActorsDao()
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

        checkSelectedMovies(id)


        binding.movieFavor.setDebouncedClickListener {
            changeSelectedState()
        }
    }

    private fun fetchMovieActors(id: Int) {
        MovieApiClient.apiClient.getMovieActorsById(id)
            .map { actors ->
                currentActors = actors.cast.toActorEntityList()
                actors.cast.map { ActorItem(it) }
            }
            .applyIoMainSchedulers()
            .subscribe({ actorList ->
                binding.actorsRecyclerView.adapter = adapter.apply { addAll(actorList) }
            }, { throwable ->
                Timber.e(throwable)
            }).let {
                compositeDisposable.add(it)
            }

    }

    private fun fetchMovieDetails(id: Int) {
        MovieApiClient.apiClient.getMovieDetailsById(id)
            .applyIoMainSchedulers()
            .subscribe({ movie ->
                currentDetailsForDb = movie.toEntity()
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
            }).let {
                compositeDisposable.add(it)
            }
    }

    private fun saveToDb(
        currentDetailsForDb: MovieEntity,
        currentActors: List<ActorEntity>
    ): Completable {
        val resultOne = movieDao.saveMovie(currentDetailsForDb)
        val resultTwo = actorDao.saveActors(currentActors)
        val resultThree = currentActors.map { actorEntity ->
            MovieActorJoin(
                movieId = currentDetailsForDb.movieId,
                actorId = actorEntity.actorId
            )
        }.let {
            movieWithActorsDao.saveJoins(it)
        }
        return resultOne.andThen(resultTwo).andThen(resultThree)
    }

    private fun changeSelectedState() {
        if (!isSelectedMovie) {
            if (::currentDetailsForDb.isInitialized && this::currentActors.isInitialized) {
                saveToDb(currentDetailsForDb, currentActors)
                    .applyIoMainSchedulers()
                    .doOnSubscribe {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    .doFinally {
                        binding.progressBar.visibility = View.GONE
                    }
                    .subscribe({
                        setNewIconSelectedState(!isSelectedMovie)
                    }, { throwable ->
                        Timber.e(throwable)
                    }).let {
                        compositeDisposable.addAll(it)
                    }

            }
        } else {
            movieDao.deleteMovieById(currentDetailsForDb.movieId)
                .applyIoMainSchedulers()
                .subscribe({
                    setNewIconSelectedState(!isSelectedMovie)
                }, { throwable ->
                    Timber.e(throwable)
                }).let {
                    compositeDisposable.addAll(it)
                }
        }
    }


    private fun setNewIconSelectedState(isSelected: Boolean) {
        isSelectedMovie = isSelected
        val drawableRes = if (isSelected) {
            R.drawable.ic_like_filled
        } else {
            R.drawable.ic_like
        }
        binding.movieFavor.setImageResource(drawableRes)
    }

    private fun checkSelectedMovies(movieId: Int) {
        movieDao.isExist(movieId)
            .applyIoMainSchedulers()
            .subscribe(
                { result ->
                    setNewIconSelectedState(result)
                },
                { throwable ->
                    Timber.e(throwable, "Error checking if movie exists")
                }
            ).let {
                compositeDisposable.add(it)
            }
    }

    private fun getMovieId() = requireArguments().getInt(FeedFragment.KEY_MOVIE_ID)

    private fun onBackPressed() {
        requireActivity().onBackPressed()
    }
}
