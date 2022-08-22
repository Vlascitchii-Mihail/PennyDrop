package com.bignerdranch.android.criminalintent

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import java.util.UUID
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext

private const val TAG = "MainActivity"

//CrimeListFragment.Callbacks - implementation of interface click listener from Fragment
//AppCompatActivity() - creating application panel
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


////    checking existence of permission
//    /**
//     * @param this - ActivityContext or ApplicationContext
//     * @param Manifest.permission.READ_CONTACTS - checked permission
//     */
//    //checkSelfPermission, returns Integer
//    private fun hasReadContactsPermission() =
//        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
//
//    //requesting permission
//    private fun requestPermission() {
//        val permissionToRequest = mutableListOf<String>()
//
//        //if hasReadContactsPermission == true skip, else requesting the permission
//        if (!hasReadContactsPermission()) permissionToRequest.add(Manifest.permission.READ_CONTACTS)
//
//        //if we are in Fragment - using requestPermissions()
//        //if we are in Activity - using ActivityCompat.requestPermissions()
//        /**
//         * @param this  - activity
//         * @param permissionToRequest.toTypedArray() - array of permissions
//         * 0 - request code
//         */
//        if (permissionToRequest.isNotEmpty()) ActivityCompat.requestPermissions(this, permissionToRequest.toTypedArray(), 0)
//    }
//
//    //checking use's answer
//    /**
//     * @param requestCode - the requestCode from ActivityCompat.requestPermissions()
//     * @param permissions the array of permissions from ActivityCompat.requestPermissions()
//     * @param grantResults : Int an array of results for each permission from the permission's array
//     */
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 0 && grantResults.isNotEmpty()) {
//
//            /**
//             * @param indices - Возвращает диапазон допустимых индексов для массива.
//             */
//            for (i in grantResults.indices) {
//
//                //executing action
//                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) Log.d(TAG, "${permissions[i]} granted")
//                else {
//                    //shouldShowRequestPermissionRationale() - returns true if user denied permission not in forever
//                    if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
//                    // TODO:   //show dialog with explanation here
//                        askUserForOpeningCallSettings()
//                    } else {
//                        // TODO:  //oops, can't do anything
//                        Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
//
//                    }
//                }
//            }
//        }
//    }
//
//    private fun askUserForOpeningCallSettings() {
//
//        //the intent for starting the application's settings
//        val appSettingsIntent = Intent(
//            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//
//            //reference to our app
//            //packageName = "MainActivity"
//            Uri.fromParts("com.bignerdranch.android.criminalintent", packageName, null)
//        )
//
//        //checking if the activity exists
//        if (packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
//            Toast.makeText(this, "Permissions are denied forever", Toast.LENGTH_SHORT).show()
//        } else {
//
//            //showing dialog
//            AlertDialog.Builder(this).setTitle("Permission denied").setMessage("You have denied permissions forever. " +
//                    "You can change your decision in app settings\n\n\"" +
//                    "Would you like to open app settings?")
//
//                //changing positive button to "Open" and opening the settings
//                .setPositiveButton("Open") {_, _ ->
//                startActivity(appSettingsIntent)
//            }.create().show()
//        }
//    }
}
