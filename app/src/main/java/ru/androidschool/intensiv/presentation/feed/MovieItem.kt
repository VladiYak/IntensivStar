package ru.androidschool.intensiv.presentation.feed

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.network.dto.MovieDto
import ru.androidschool.intensiv.databinding.ItemWithTextBinding
import ru.androidschool.intensiv.domain.entity.MovieEntity
import ru.androidschool.intensiv.utils.loadImage

class MovieItem(
    private val content: MovieEntity,
    private val onClick: (movie: MovieEntity) -> Unit
) : BindableItem<ItemWithTextBinding>() {

    override fun getLayout(): Int = R.layout.item_with_text

    override fun bind(view: ItemWithTextBinding, position: Int) {
        view.description.text = content.title
        view.movieRating.rating = content.rating
        view.content.setOnClickListener {
            onClick.invoke(content)
        }

        view.imagePreview.loadImage(content.posterUrl)
    }

    override fun initializeViewBinding(view: View) = ItemWithTextBinding.bind(view)
}
