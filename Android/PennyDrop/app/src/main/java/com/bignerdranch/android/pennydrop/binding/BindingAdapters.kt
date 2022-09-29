package com.bignerdranch.android.pennydrop.binding

import android.view.View
import androidx.databinding.BindingAdapter


/**
 * create new attribute isHidden in the each View
 * автономная функция уровня пакетар
 */
@BindingAdapter("isHidden")
fun bindIsHidden(view: View, isInvisible: Boolean) {
    view.visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
}