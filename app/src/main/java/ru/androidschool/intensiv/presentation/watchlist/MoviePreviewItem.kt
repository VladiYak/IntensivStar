package ru.androidschool.intensiv.presentation.watchlist

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.databinding.ItemSmallBinding
import ru.androidschool.intensiv.data.local.MovieWithActors
import ru.androidschool.intensiv.data.local.entities.MovieDbEntity
import ru.androidschool.intensiv.utils.loadImage

class MoviePreviewItem(
    private val content: MovieWithActors,
    private val onClick: (movieEntity: MovieDbEntity) -> Unit
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
