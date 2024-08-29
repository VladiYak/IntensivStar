package ru.androidschool.intensiv.ui.watchlist

import android.view.View
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.databinding.ItemSmallBinding
import ru.androidschool.intensiv.local.MovieWithActors
import ru.androidschool.intensiv.local.entities.MovieEntity
import ru.androidschool.intensiv.utils.loadImage

class MoviePreviewItem(
    private val content: MovieWithActors,
    private val onClick: (movieEntity: MovieEntity) -> Unit
) : BindableItem<ItemSmallBinding>() {

    override fun getLayout() = R.layout.item_small

    override fun bind(view: ItemSmallBinding, position: Int) {
        view.imagePreview.setOnClickListener {

        }
        view.textPreviewName.text = content.movie.title
        view.imagePreview.loadImage(content.movie.posterPath)
    }

    override fun initializeViewBinding(v: View): ItemSmallBinding = ItemSmallBinding.bind(v)
}
