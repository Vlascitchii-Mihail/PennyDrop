package com.bignerdranch.android.criminalintent

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.UUID
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

private const val TAG = "MainActivity"

//CrimeListFragment.Callbacks - implementation of interface click listener from Fragment
class MainActivity : AppCompatActivity(), CrimeListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime)

        /**
         * @param supportFragmentManager - access to FragmentManager
         */
        //receiving an exemplar of fragment from FragmentManager after recreating of the layout
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
//            val fragment = CrimeFragment()

            //creates a new object of fragment CrimeListFragment
            val fragment = CrimeListFragment.newInstance()

            //beginTransaction() - creates and return an exemplar of FragmentTransaction
            //add() - filling the exemplar of FragmentTransaction
            /**
             *@param R.id.fragment_container - container ID
             * @param fragment - fragment exemplar
             */
            //starts fragment functions onCreate(), onCreateView(), onViewCreate(), 0nAttach()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment)
                .commit()
        }
    }


    //colling from fragment uses a callback
    override fun onCrimeSelected(crimeId: UUID) {
//            Log.d(TAG, "MainActivity.onCrimeSelected: $crimeId")

        //creating new CrimeFragment's object with ID
        val fragment = CrimeFragment.newInstance(crimeId)

        //calling a new fragment uses the crime.Id
        //replace(R.id.fragment_container, fragment) - changing the old fragment to the new fragment
        //addToBackStack(null) - returning to the previous fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
    }


//    private fun hasReadContactsPermission() =
//        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
//
//    private fun requestPermission() {
//        val permissionToRequest = mutableListOf<String>()
//        if (!hasReadContactsPermission()) permissionToRequest.add(Manifest.permission.READ_CONTACTS)
//
//        if (permissionToRequest.isNotEmpty()) ActivityCompat.requestPermissions(this, permissionToRequest.toTypedArray(), 0)
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 0 && grantResults.isNotEmpty()) {
//            for (i in grantResults.indices) {
//                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) Log.d(TAG, "${permissions[i]} granted")
//            }
//        }
//    }
}