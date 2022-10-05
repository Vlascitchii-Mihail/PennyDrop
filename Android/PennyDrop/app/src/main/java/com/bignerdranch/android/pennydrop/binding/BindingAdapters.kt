package com.bignerdranch.android.pennydrop.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.Converters.convertColorToColorStateList
import com.bignerdranch.android.pennydrop.R


/**
 * create new attribute isHidden in the each View
 * автономная функция уровня пакетар
 */
@BindingAdapter("isHidden")
fun bindIsHidden(view: View, isInvisible: Boolean) {
    view.visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
}

/**
 * change the image of the user or of the AI in the Ranking list
 */
@BindingAdapter("playerSummaryAvatarSrc")
fun bindPlayerSummaryAvatarSrc(imageView: ImageView, isHuman: Boolean) {
    imageView.setImageResource(
        if (isHuman) R.drawable.ic_baseline_tag_faces_24
    else R.drawable.android_24
    )
}

/**
 * change the tint(оттенок) of the image
 */
@BindingAdapter("playerSummaryAvatarTint")
fun bindPlayerSummaryAvatarTint(imageView: ImageView, isHuman: Boolean) {

//    convertColorToColorStateList() - Converts int color into a ColorStateList
    //ColorStateList - Позволяет сопоставлять наборы состояний просмотра с цветами.
    imageView.imageTintList = convertColorToColorStateList(

        //ColorStateList - Позволяет сопоставлять наборы состояний просмотра с цветами.
        imageView.context.getColor(
            if (isHuman) android.R.color.holo_blue_bright
        else android.R.color.holo_green_light
        )
    )
}













