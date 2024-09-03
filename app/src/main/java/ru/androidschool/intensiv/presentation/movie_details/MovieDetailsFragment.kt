package ru.androidschool.intensiv.presentation.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Completable
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.databinding.MovieDetailsFragmentBinding
import ru.androidschool.intensiv.data.local.entities.MovieActorJoin
import ru.androidschool.intensiv.data.local.MoviesDatabase
import ru.androidschool.intensiv.data.local.dao.ActorDao
import ru.androidschool.intensiv.data.local.dao.MovieDao
import ru.androidschool.intensiv.data.local.dao.MovieWithActorsDao
import ru.androidschool.intensiv.data.local.entities.ActorDbEntity
import ru.androidschool.intensiv.data.local.entities.MovieDbEntity
import ru.androidschool.intensiv.data.mappers.ActorMapper
import ru.androidschool.intensiv.data.mappers.DbActorsMapper
import ru.androidschool.intensiv.data.mappers.DbMovieDetailsMapper
import ru.androidschool.intensiv.data.mappers.MovieDetailsMapper
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.data.network.dto.MovieDetails
import ru.androidschool.intensiv.data.repository.ActorsRepositoryImpl
import ru.androidschool.intensiv.data.repository.MovieDetailsRepositoryImpl
import ru.androidschool.intensiv.data.repository.SelectedMovieRepositoryImpl
import ru.androidschool.intensiv.domain.entity.MovieDetailsEntity
import ru.androidschool.intensiv.domain.repository.ActorsRepository
import ru.androidschool.intensiv.domain.repository.MovieDetailsRepository
import ru.androidschool.intensiv.domain.repository.SelectedMovieRepository
import ru.androidschool.intensiv.presentation.base.BaseFragment
import ru.androidschool.intensiv.presentation.feed.FeedFragment
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

    private val movieDetailsInfoMapper = MovieDetailsMapper()
    private val actorMapper = ActorMapper()
    private val dbMovieDetailsMapper = DbMovieDetailsMapper()
    private val dbActorsMapper = DbActorsMapper()

    private var isSelectedMovie = false

    private lateinit var currentDetailsForDb: MovieDbEntity
    private lateinit var currentActors: List<ActorDbEntity>

    private lateinit var selectedMovieRepository: SelectedMovieRepository
    private lateinit var movieDetailsRepository: MovieDetailsRepository
    private lateinit var actorsRepository: ActorsRepository

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

        selectedMovieRepository = SelectedMovieRepositoryImpl(requireContext())
        movieDetailsRepository = MovieDetailsRepositoryImpl()
        actorsRepository = ActorsRepositoryImpl()

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
        actorsRepository.getActorsById(id)
            .applyIoMainSchedulers()
            .subscribe({ actorList ->
                val newActorListEntities = actorList.cast.map { cast ->
                    actorMapper.mapTo(cast)
                }
                currentActors = dbActorsMapper.mapTo(newActorListEntities)
                newActorListEntities.map { singleActorInfo ->
                    ActorItem(singleActorInfo)
                }.let { actorItem ->
                    binding.actorsRecyclerView.adapter = adapter.apply { addAll(actorItem) }
                }
            }, { throwable ->
                Timber.e(throwable)
            }).let {
                compositeDisposable.add(it)
            }

    }

    private fun fetchMovieDetails(id: Int) {
        movieDetailsRepository.getMovieDetails(id)
            .applyIoMainSchedulers()
            .subscribe({ movie ->
                movieDetailsInfoMapper.mapTo(movie)
                    .also { movieDetail ->
                        currentDetailsForDb = dbMovieDetailsMapper.mapTo(movieDetail)
                        bindDetails(movieDetail)
                    }
            }, { throwable ->
                Timber.e(throwable)
            }).let {
                compositeDisposable.add(it)
            }
    }

    private fun bindDetails(movie: MovieDetailsEntity) {
        with(binding) {
            movieTitle.text = movie.movieName
            movieRating.rating = movie.movieRating
            movieDescription.text = movie.movieDescription
            movieStudio.text =
                movie.studioName
            movieGenre.text = movie.genre
            movieYear.text = movie.year
            movieImage.loadImage(movie.posterPath)
        }
    }

    private fun changeSelectedState() {
        if (!isSelectedMovie) {
            if (::currentDetailsForDb.isInitialized && this::currentActors.isInitialized) {
                selectedMovieRepository.saveToDb(currentDetailsForDb, currentActors)
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
            selectedMovieRepository.deleteMovieById(currentDetailsForDb.movieId)
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
        selectedMovieRepository.checkSavedMovieById(movieId)
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
