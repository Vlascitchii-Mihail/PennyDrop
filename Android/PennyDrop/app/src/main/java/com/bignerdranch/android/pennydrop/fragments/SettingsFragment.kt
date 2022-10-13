package com.bignerdranch.android.pennydrop.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.preference.DropDownPreference
import androidx.preference.ListPreference
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



        //Finds a Preference with the given key. Returns null if
        // no Preference could be found with the given key.
        val themeModePreference = findPreference<ListPreference?>("themeMode")

        //setDefaultValue() - Sets the default value for this preference, which will be set either
             // if persistence is off or persistence is on and the preference is not found in the persistent storage.
        //AppCompatDelegate - This class represents a delegate which you can use to extend AppCompat's support to any Activity
        themeModePreference?.setDefaultValue(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)


        //OnPreferenceChangeListener {} - Sets the callback to be invoked when this
        // preference is changed by the user.
        themeModePreference?.onPreferenceChangeListener =

            //newValue - user's theme choice from ListPreference in settings.xml
            Preference.OnPreferenceChangeListener { _, newValue ->
                val nightMode = when(newValue?.toString()) {
                    "Light" -> AppCompatDelegate.MODE_NIGHT_NO
                    "Dark" -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }

                //Sets the default night mode immediately.
                AppCompatDelegate.setDefaultNightMode(nightMode)
                true
            }



        //Finds a Preference with the given key. Returns null if
        // no Preference could be found with the given key.
        val creditsPreferences = findPreference<Preference?>("credits")

        //click listener
        creditsPreferences?.onPreferenceClickListener =

            //change the fragment
            Preference.OnPreferenceClickListener { _ ->
                this.findNavController().navigate(R.id.aboutFragment)
                true
            }
    }
}