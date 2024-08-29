package ru.androidschool.intensiv.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import ru.androidschool.intensiv.data.MockRepository
import ru.androidschool.intensiv.databinding.FragmentWatchlistBinding
import ru.androidschool.intensiv.local.MoviesDatabase
import ru.androidschool.intensiv.local.dao.MovieWithActorsDao
import ru.androidschool.intensiv.ui.BaseFragment
import ru.androidschool.intensiv.utils.applyIoMainSchedulers
import timber.log.Timber

class WatchlistFragment : BaseFragment() {

    private var _binding: FragmentWatchlistBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val disposables = CompositeDisposable()
    private lateinit var moviesDb: MoviesDatabase
    private lateinit var movieWithActorsDao: MovieWithActorsDao

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moviesDb = MoviesDatabase.getDatabase(requireContext())
        movieWithActorsDao = moviesDb.movieWithActorsDao()
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

        binding.moviesRecyclerView.layoutManager = GridLayoutManager(context, 4)
        binding.moviesRecyclerView.adapter = adapter.apply { addAll(listOf()) }

        movieWithActorsDao.getAllSelectedMovies()
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
