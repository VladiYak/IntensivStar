package ru.androidschool.intensiv.ui.search

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
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.databinding.FragmentSearchBinding
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.feed.FeedFragment.Companion.KEY_SEARCH
import ru.androidschool.intensiv.ui.feed.MovieItem
import ru.androidschool.intensiv.utils.applyIoMainSchedulers
import ru.androidschool.intensiv.utils.withProgressBar
import timber.log.Timber

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private var _searchBinding: FeedHeaderBinding? = null

    val disposables = CompositeDisposable()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchBinding get() = _searchBinding!!

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        _searchBinding = FeedHeaderBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchTerm = requireArguments().getString(KEY_SEARCH)
        searchBinding.searchToolbar.setText(searchTerm)

        searchMovie(searchTerm)
    }

    private fun searchMovie(searchTerm: String?) {
        binding.moviesRecyclerView.adapter = adapter
        MovieApiClient.apiClient.searchMovie(
            query = searchTerm ?: ""
        )
            .map { response ->
                response.results?.map {
                    MovieItem(it) {

                    }
                }
            }
            .applyIoMainSchedulers()
            .withProgressBar(binding.progressBar.progressBar)
            .subscribe({ movies ->
                adapter.apply {
                    addAll(movies ?: listOf())
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
        _searchBinding = null
        disposables.clear()
    }
}
