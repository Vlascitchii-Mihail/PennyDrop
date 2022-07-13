package com.bignerdranch.android.beatbox

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.DataBindingUtil
import com.bignerdranch.android.beatbox.databinding.ActivityMainBinding

class SoundViewModel(private val beatBox: BeatBox): BaseObservable() {

    fun onButtonClicked() {
        sound?.let {
            beatBox.play(it)
        }
    }

    var sound: Sound? = null
    set(sound) {
        field = sound
        notifyChange()
    }

    @get: Bindable
    val title: String?
    get() = sound?.name
}
