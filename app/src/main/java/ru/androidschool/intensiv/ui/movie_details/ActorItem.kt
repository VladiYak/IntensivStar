package ru.androidschool.intensiv.ui.movie_details

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.Cast
import ru.androidschool.intensiv.databinding.ItemActorBinding
import ru.androidschool.intensiv.utils.loadImage

class ActorItem(
    private val content: Cast
) : BindableItem<ItemActorBinding>() {
    override fun bind(view: ItemActorBinding, position: Int) {
        view.actorName.text = content.name

        view.actorPreview.loadImage(content.profilePath)
    }

    override fun getLayout(): Int = R.layout.item_actor

    override fun initializeViewBinding(view: View) = ItemActorBinding.bind(view)

}