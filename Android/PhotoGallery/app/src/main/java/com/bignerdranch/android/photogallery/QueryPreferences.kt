package com.bignerdranch.android.photogallery

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit

//key for the query
private const val PREF_SEARCH_QUERY = "searchQuery"
private const val PREF_LAST_RESULT_ID = "lastResultId"
private const val PREF_IS_POLLING = "isPolling"

//interface for saving data in the class SharedPreferences
object QueryPreferences {

    //uploading data from the SharedPreferences
    fun getStoredQuery(context: Context): String {

        //returns an exemplar of the SharedPreferences
        val pref = PreferenceManager.getDefaultSharedPreferences(context)

        //"" - default string
        return pref.getString(PREF_SEARCH_QUERY, "")!!
    }

    //writing data in the SharedPreferences
    fun setStoredQuery(context: Context, query: String) {

        //edit{} - Allows editing of this preference instance with a call to
        // apply or commit to save the changes. Default behaviour is apply.
        //executes in the background thread
        PreferenceManager.getDefaultSharedPreferences(context).edit{
            putString(PREF_SEARCH_QUERY, query)
        }
    }

    fun getLastResultId(context: Context) : String {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(PREF_LAST_RESULT_ID, "")!!
    }

    fun setLastResultId(context: Context, lastResultId: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putString(PREF_LAST_RESULT_ID, lastResultId)
        }
    }

    fun isPolling(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(PREF_IS_POLLING, false)
    }

    fun setPolling(context: Context, isOn: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putBoolean(PREF_IS_POLLING, isOn)
        }
    }
}