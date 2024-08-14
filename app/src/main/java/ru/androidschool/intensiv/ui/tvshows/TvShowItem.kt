package ru.androidschool.intensiv.ui.tvshows

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.TvShowDto
import ru.androidschool.intensiv.databinding.ItemTvShowBinding
import ru.androidschool.intensiv.utils.loadImage

class TvShowItem(
    private val content: TvShowDto,
    private val onClick: (tvShow: TvShowDto) -> Unit
) : BindableItem<ItemTvShowBinding>() {

    override fun getLayout(): Int = R.layout.item_tv_show

    override fun bind(view: ItemTvShowBinding, position: Int) {
        view.title.text = content.name
        view.movieRating.rating = content.voteAverage?.toFloat() ?: 0.0f
        view.content.setOnClickListener {
            onClick.invoke(content)
        }

        view.imagePreview.loadImage(content.posterPath)
    }

    override fun initializeViewBinding(view: View) = ItemTvShowBinding.bind(view)
}