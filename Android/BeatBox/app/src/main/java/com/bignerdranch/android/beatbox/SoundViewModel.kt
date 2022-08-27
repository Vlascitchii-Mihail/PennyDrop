package com.bignerdranch.android.beatbox

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.DataBindingUtil
import com.bignerdranch.android.beatbox.databinding.ActivityMainBinding

//transfers data to list_item_sound
//Observable() - Наблюдаемые классы предоставляют способ, с помощью которого пользовательский интерфейс с привязкой к данным может быть уведомлен об изменениях.
//BaseObservable() - Используется при некорректном обновлении макета представления,
// если макет не имеет возможности узнать что мы обновили объект Sound принадлежащий SoundViewModel
//внутри SoundHolder.bind(Sound)
//BaseObservable() - data binding interface
class SoundViewModel(private val beatBox: BeatBox): BaseObservable() {

    var sound: Sound? = null
    set(sound) {
        field = sound

        //calls on every change binding property values
        //Уведомляет слушателей об изменении всех свойств этого экземпляра.
        //calls "@{ () -> viewModel.onButtonClicked() }" from list_item_sound при любом изменкниив list_item_sound
        //Для повторного заполнения представления
        notifyChange()
    }

    //@get: Bindable - using for initializing buttons' names in list_item_sound
    //@get: Bindable -
    @get: Bindable
    val title: String?
    get() = sound?.name

    fun onButtonClicked() {

        //playing sounds when the button was clicked
        sound?.let {
            beatBox.play(it)
        }
    }
}
