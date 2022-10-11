package com.bignerdranch.android.pennydrop.fragments

import android.os.Bundle
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.bignerdranch.android.pennydrop.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        //add the preference's XML file in preference's fragment
        setPreferencesFromResource(R.xml.preferences, rootKey)

        //Finds a Preference with the given key. Returns null if
        // no Preference could be found with the given key.
        val themePreference = findPreference<DropDownPreference?>("theme")

        //recreate the activity when we change the theme in the DropDownPreference
        //OnPreferenceChangeListener {} - Sets the callback to be invoked when this
        // preference is changed by the user (but before the internal state has been updated).
        themePreference?.onPreferenceChangeListener =

            //DropDownPreference listener
            Preference.OnPreferenceChangeListener { _, _ ->

                //recreate() - Cause this Activity to be recreated with a new instance.
                activity?.recreate()
                true
            }
    }
}