package com.bignerdranch.android.geoquiz_2

import androidx.annotation.StringRes

//@StringRes помогает встроенному в Android Studio инспектору кода (под
//названием Lint) проверить во время компиляции, что в
//конструкторе используется правильный строковый идентификатор ресурса
data class Question(@StringRes val textResId: Int, val answer: Boolean)