package ru.androidschool.intensiv.presentation.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import ru.androidschool.intensiv.databinding.FragmentWatchlistBinding
import ru.androidschool.intensiv.data.local.MoviesDatabase
import ru.androidschool.intensiv.data.local.dao.MovieWithActorsDao
import ru.androidschool.intensiv.data.repository.SelectedMovieRepositoryImpl
import ru.androidschool.intensiv.domain.repository.SelectedMovieRepository
import ru.androidschool.intensiv.presentation.base.BaseFragment
import ru.androidschool.intensiv.utils.applyIoMainSchedulers
import timber.log.Timber

class WatchlistFragment : BaseFragment() {

    private var _binding: FragmentWatchlistBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var selectedMovieRepository: SelectedMovieRepository

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedMovieRepository = SelectedMovieRepositoryImpl(requireContext())

        binding.moviesRecyclerView.layoutManager = GridLayoutManager(context, 4)
        binding.moviesRecyclerView.adapter = adapter.apply { addAll(listOf()) }

        selectedMovieRepository.getSelectedMovies()
            .applyIoMainSchedulers()
            .subscribe({
                it.map { movieWithActors ->
                    MoviePreviewItem(movieWithActors) { movie -> }
                }.toList().also { moviesFromDb ->
                    binding.moviesRecyclerView.adapter = adapter.apply { addAll(moviesFromDb) }
                }
            }, { throwable ->
                Timber.e(throwable)
            }).let { compositeDisposable.addAll(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = WatchlistFragment()
    }
}
