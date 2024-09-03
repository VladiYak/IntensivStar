package ru.androidschool.intensiv.presentation.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.network.dto.TvShows
import ru.androidschool.intensiv.databinding.TvShowsFragmentBinding
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.data.repository.TvShowsRepositoryImpl
import ru.androidschool.intensiv.domain.repository.TvShowsRepository
import ru.androidschool.intensiv.utils.applyIoMainSchedulers
import ru.androidschool.intensiv.utils.withProgressBar
import timber.log.Timber

class TvShowsFragment : Fragment(R.layout.tv_shows_fragment) {

    private var _binding: TvShowsFragmentBinding? = null
    private val binding get() = _binding!!

    val disposables = CompositeDisposable()

    private lateinit var tvShowsRepository: TvShowsRepository

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TvShowsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvShowsRepository = TvShowsRepositoryImpl()

        binding.tvShowRecyclerView.adapter = adapter

        fetchPopularTvShows()

    }

    private fun fetchPopularTvShows() {
        val getPopularTvShows = tvShowsRepository.getPopularTvShows()
        loadAndShowTvShows(getPopularTvShows)
    }

    private fun loadAndShowTvShows(getTvShows: Single<TvShows>) {
        getTvShows
            .map { tvShows ->
                tvShows.results?.map {
                    TvShowItem(it) {

                    }
                }
            }
            .applyIoMainSchedulers()
            .withProgressBar(binding.progressBar.progressBar)
            .subscribe({ tvShows ->
                adapter.apply {
                    addAll(tvShows ?: listOf())
                }
            }, { throwable ->
                Timber.e(throwable)
            }).let {
                disposables.add(it)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposables.clear()
    }
}
