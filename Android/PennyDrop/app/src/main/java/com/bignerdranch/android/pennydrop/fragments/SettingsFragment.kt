package com.bignerdranch.android.pennydrop.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.bignerdranch.android.pennydrop.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        //add the preference's XML file in preference's fragment
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}