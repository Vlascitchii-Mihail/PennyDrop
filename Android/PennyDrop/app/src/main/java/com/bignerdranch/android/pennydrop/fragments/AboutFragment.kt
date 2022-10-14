package com.bignerdranch.android.pennydrop.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bignerdranch.android.pennydrop.R
import com.bignerdranch.android.pennydrop.databinding.FragmentAboutBinding

/**
 * information about app
 */
class AboutFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentAboutBinding.inflate(inflater, container, false).apply {

            //movementMethod - Задает метод MovementMethod для обработки перемещения клавиши со стрелкой для этого TextView.
            //LinkMovementMethod - Метод перемещения, который перемещается по ссылкам (есди они есть) в текстовом
            // буфере и при необходимости прокручивается.
            aboutCredits.movementMethod = LinkMovementMethod.getInstance()


            //movementMethod - Задает метод MovementMethod для обработки перемещения клавиши со стрелкой для этого TextView.
            //LinkMovementMethod - Метод перемещения, который перемещается по ссылкам (есди они есть) в текстовом
            // буфере и при необходимости прокручивается.
            aboutIconsCredits.apply {

                //highlighting link words in the text
                //SpannableString - allow to change pieces of text: background, foreground, colors, styling, convert to link
                val spannableString = SpannableString(getString(R.string.penny_drop_icons))

                //modify the string
                spannableString.setSpan(

                    //Constructs a URLSpan  from a url string, numbers are the indices of Char Sequence
                    URLSpan("https://materialdesignicons.com/icon/currency-usd-circle-outline"),
                    4,8,0
                )

                //modify the string
                spannableString.setSpan(

                    //Constructs a URLSpan  from a url string, numbers are the indices of Char Sequence
                    URLSpan("https://materialdesigncons.com/icon/dice-6"),
                    13, 26, 0
                )

                //modify the string
                spannableString.setSpan(

                    //Constructs a URLSpan  from a url string, numbers are the indices of Char Sequence
                    URLSpan("https://materialdesignicons.com"),
                    46, 67, 0
                )

                //add the text from spannableString to text widget
                this.text = spannableString


                //movementMethod - Задает метод MovementMethod для обработки перемещения клавиши со стрелкой для этого TextView.
                //LinkMovementMethod - Метод перемещения, который перемещается по ссылкам (есди они есть) в текстовом
                // буфере и при необходимости прокручивается.
                this.movementMethod = LinkMovementMethod.getInstance()
            }
        }


        return binding.root
    }
}