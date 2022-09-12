package com.bignerdranch.android.beatbox

import androidx.lifecycle.ViewModel
import android.content.res.AssetManager
import kotlin.coroutines.coroutineContext

class ViewModelSound (assets: AssetManager): ViewModel() {
    var beatBox = BeatBox(assets)

    fun saveBeat(beat: BeatBox) {
        beatBox = beat
    }

    override fun onCleared() {
        super.onCleared()
        beatBox.release()
    }
}