package com.bignerdranch.android.pennydrop.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.pennydrop.types.NewPlayer

class PickPlayersViewModel : ViewModel() {
    val players = MutableLiveData<List<NewPlayer>>().apply {
        this.value = (1 .. 6).map {
            NewPlayer(canBeRemoved = it > 2, canBeToggled = it > 1)
        }
    }
}