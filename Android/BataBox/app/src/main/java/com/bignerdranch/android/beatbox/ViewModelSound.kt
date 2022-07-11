package com.bignerdranch.android.beatbox

import androidx.lifecycle.ViewModel
import android.content.res.AssetManager
import kotlin.coroutines.coroutineContext

class ViewModelSound: ViewModel() {
    val beatBox: BeatBox = BeatBox(MainActivity().assets)


}