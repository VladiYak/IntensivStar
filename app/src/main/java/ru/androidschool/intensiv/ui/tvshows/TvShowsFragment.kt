package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.MockRepository
import ru.androidschool.intensiv.data.dto.Movies
import ru.androidschool.intensiv.data.dto.TvShows
import ru.androidschool.intensiv.databinding.TvShowsFragmentBinding
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.feed.MainCardContainer
import ru.androidschool.intensiv.ui.feed.MovieItem
import timber.log.Timber

class TvShowsFragment : Fragment(R.layout.tv_shows_fragment) {

    private var _binding: TvShowsFragmentBinding? = null
    private val binding get() = _binding!!

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

        binding.tvShowRecyclerView.adapter = adapter

        fetchPopularTvShows()

    }

    private fun fetchPopularTvShows() {
        val getPopularTvShows = MovieApiClient.apiClient.getPopularTvShows()
        loadAndShowTvShows(getPopularTvShows, R.string.popular)
    }

    private fun loadAndShowTvShows(getTvShows: Call<TvShows>, @StringRes title: Int) {
        getTvShows.enqueue(object : Callback<TvShows> {
            override fun onResponse(call: Call<TvShows>, response: Response<TvShows>) {
                response.body()?.results?.let { results ->
                    val tvShows = results.map {
                        TvShowItem(it) {
                        }
                    }
                    adapter.apply { addAll(tvShows) }
                }
            }

            override fun onFailure(call: Call<TvShows>, t: Throwable) {
                Timber.e(t)
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
