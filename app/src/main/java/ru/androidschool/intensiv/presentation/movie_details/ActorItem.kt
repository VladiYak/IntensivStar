package ru.androidschool.intensiv.presentation.movie_details

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.network.dto.Cast
import ru.androidschool.intensiv.databinding.ItemActorBinding
import ru.androidschool.intensiv.domain.entity.ActorInfoEntity
import ru.androidschool.intensiv.utils.loadImage

class ActorItem(
    private val content: ActorInfoEntity
) : BindableItem<ItemActorBinding>() {
    override fun bind(view: ItemActorBinding, position: Int) {
        view.actorName.text = content.fullName

        view.actorPreview.loadImage(content.imageUrl)
    }

    override fun getLayout(): Int = R.layout.item_actor

    override fun initializeViewBinding(view: View) = ItemActorBinding.bind(view)

}