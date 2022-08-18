package com.bignerdranch.android.criminalintent

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeBinding
import android.content.Intent.ACTION_DIAL
import android.provider.MediaStore
import android.widget.*
import java.io.File
import androidx.core.content.FileProvider
import android.content.pm.ResolveInfo
import android.view.ViewTreeObserver
import java.util.*


private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"
private const val DIALOG_DATE = "DialogDate"
const val REQUEST_KEY = "request"
private const val DATA_FORMAT = "EEE, d MMM yyyy HH:mm:ss"
private const val REQUEST_CONTACT = 1
private const val CALL_REQUEST_CODE = 0
private const val REQUEST_PHOTO = 2

//:Fragment() transform class to fragment
class CrimeFragment : Fragment() {
    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var reportButton: Button
    private lateinit var suspectButton: Button
    private lateinit var callButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri
    private lateinit var treeObserver: ViewTreeObserver
    var viewWidth = 0
    var viewHeight = 0

    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {
        ViewModelProvider(this)[CrimeDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()

        //providing access to fragment's arguments and getting crime.Id
        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
//        Log.d(TAG, "args bundle crime ID: $crimeId")
        crimeDetailViewModel.loadCrime(crimeId)
    }

    //creation and setting the view of fragment
    //returns view to host activity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //filling view of the fragment
        /**
         * @param R.layout.fragment_crime id of the layout
         * @param container - parent of the view
         * @param false нужно ли включать заполненное предсав. родитедя?
         */
        val view = inflater.inflate(R.layout.fragment_crime, container, false)

        //connected buttons and textViews using view
        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
        timeButton = view.findViewById(R.id.crime_time) as Button
        reportButton = view.findViewById(R.id.crime_report) as Button
        suspectButton = view.findViewById(R.id.crime_suspect) as Button
        callButton = view.findViewById(R.id.call_button) as Button
        photoButton = view.findViewById(R.id.crime_camera) as ImageButton
        photoView = view.findViewById(R.id.crime_photo) as ImageView
        photoView.isEnabled = false
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crimeDetailViewModel.crimeLiveData.observe(viewLifecycleOwner, Observer { crime -> crime?.let {
            this.crime = crime
            photoFile = crimeDetailViewModel.getPhotoFile(crime)
            photoUri = FileProvider.getUriForFile(requireActivity(), "com.bignerdranch.android.criminalintent.fileprovider", photoFile)
            updateUI()
        }})

        childFragmentManager.setFragmentResultListener(REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
            crime.date = bundle.getSerializable("returnData") as Date
            updateUI()
        }

        childFragmentManager.setFragmentResultListener(REQUEST_KEY + 1, viewLifecycleOwner) { _, bundle ->
            crime.date = bundle.getSerializable("returnTime") as Date
            updateUI()
        }
    }

    override fun onStart() {
        super.onStart()

        //editTextListener
        //object : TextWatcher anonymous class which implements interface TextWatcher
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {}

            /**
             * @param sequence - user's input
             */
            //creates title of each crime
            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {}
        }

        titleField.addTextChangedListener(titleWatcher)

        solvedCheckBox.apply {

            ////check box listener
            setOnCheckedChangeListener {
                    _, isChecked ->
                crime.isSolved = isChecked
            }
        }

        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(crime.date).apply {
                show(this@CrimeFragment.childFragmentManager, DIALOG_DATE)
            }
        }

        timeButton.setOnClickListener {
            TimePickerFragment.newInstanceTime(crime.date).apply {
                show(this@CrimeFragment.childFragmentManager, DIALOG_DATE)
            }
        }

        photoView.setOnClickListener {
            PhotoZoomFragment.newInstance(photoFile).apply {
                show(this@CrimeFragment.childFragmentManager, DIALOG_DATE)
            }
        }

        reportButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/playn"
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
            }.also { intent ->

                ////////////////old method
//                startActivity(intent)
                val chooserIntent = Intent.createChooser(intent, getString(R.string.send_report))
                startActivity(chooserIntent)
            }
        }

        suspectButton.apply {
            //    val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            setOnClickListener {
                ////////////////old method
//                startActivityForResult(pickContactIntent, REQUEST_CONTACT)
                contactsPermissionRequestLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }

        callButton.setOnClickListener {
            callPermissionRequestLauncher.launch(Manifest.permission.CALL_PHONE)
        }

//        photoButton.apply {
//            val packageManager: PackageManager = requireActivity().packageManager
//            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
//
//            if (resolvedActivity == null) isEnabled = false
//
//            setOnClickListener {
//                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//                val cameraActivities: List<ResolveInfo> = packageManager.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
//
//                for (cameraActivity in cameraActivities) {
//                    requireActivity().grantUriPermission(cameraActivity.activityInfo.packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//                }
//
//                startActivityForResult(captureImage, REQUEST_PHOTO)
//            }
//        }

        photoButton.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY)

            if (resolvedActivity == null) isEnabled = false

            setOnClickListener {
                cameraPermissionRequestLauncher.launch(Manifest.permission.CAMERA)
            }

        }
    }

    ////////////////old method
//    @Deprecated("Deprecated in Java", ReplaceWith(
//        "super.onRequestPermissionsResult(requestCode, permissions, grantResults)",
//        "androidx.fragment.app.Fragment"
//    )
//    )
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            CALL_REQUEST_CODE -> { if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                permissionGranted()
//                 } else {
//                     if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
//                         Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_LONG).show()
//                     } else askUserForOpeningAppSettings()
//                 }
//            }
//        }
//    }
//

    private val callPermissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(), ::onGotPermissionCall)

    private fun onGotPermissionCall(granted: Boolean) {
        if (granted) permissionCallGranted()
        else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_LONG).show()
            } else askUserForOpeningCallSettings()
        }
    }

    private fun permissionCallGranted() {
//        Toast.makeText(requireContext(), "Contact permission is granted", Toast.LENGTH_SHORT).show()
        startActivity(Intent(ACTION_DIAL, Uri.parse("tel:" +crime.suspectPhoneNumber)))
    }

    private fun askUserForOpeningCallSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("com.bignerdranch.android.criminalintent", "MainActivity", "CrimeFragment")
        )
        if (requireActivity().packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            Toast.makeText(requireContext(), "Permissions are denied forever", Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(context).setTitle("Permission denied").setMessage("You have denied permissions forever. " +
                    "You can change your decision in app settings\n\n\"" +
                    "Would you like to open app settings?").setPositiveButton("Open") {_, _ ->
                startActivity(appSettingsIntent)
            }.create().show()
        }
    }





    private val contactsPermissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(), ::onGotPermissionContact)

    private fun onGotPermissionContact(granted: Boolean) {
        if (granted) permissionContactsGranted()
        else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_LONG).show()
            } else askUserForOpeningContactSettings()
        }
    }

    private fun permissionContactsGranted() {
//        Toast.makeText(requireContext(), "Contact permission is granted", Toast.LENGTH_SHORT).show()
        pickContact.launch(null)
    }

    private fun askUserForOpeningContactSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("com.bignerdranch.android.criminalintent", "MainActivity", "CrimeFragment")
        )
        if (requireActivity().packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            Toast.makeText(requireContext(), "Permissions are denied forever", Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(context).setTitle("Permission denied").setMessage("You have denied permissions forever. " +
                    "You can change your decision in app settings\n\n\"" +
                    "Would you like to open app settings?").setPositiveButton("Open") {_, _ ->
                startActivity((appSettingsIntent))
            }.create().show()
        }
    }

    //    val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
    private val pickContact = registerForActivityResult(ActivityResultContracts.PickContact()) { contactUri ->
        if (contactUri != null) {
            getContactNameAndID(contactUri)
        }
    }

    private fun getContactNameAndID(uri: Uri) {
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID)
        val cursorNameAndId = uri.let {
            requireActivity().contentResolver.query(it, queryFields, null, null, null)
        }
        cursorNameAndId?.use {
            if(it.count > 0) {
                it.moveToFirst()
                val suspect = it.getString(0)
                crime.suspect = suspect
                suspectButton.text = suspect

                crime.suspectPhoneNumber = phoneNumber(it.getString(1))
                callButton.text = crime.suspectPhoneNumber
                crimeDetailViewModel.saveCrime(crime)
            }
        }
    }

    //take contact ID and receive the phone number
    private fun phoneNumber(str: String): String {
        var number = ""
        val phoneURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val phoneNumberQueryFields = listOf(ContactsContract.CommonDataKinds.Phone.NUMBER).toTypedArray()

        // phoneWhereClause: A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself)
        val phoneWhereClause = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
        val phoneQueryParameters = listOf(str).toTypedArray()
        val phoneCursor = requireActivity().contentResolver.query(phoneURI, phoneNumberQueryFields, phoneWhereClause, phoneQueryParameters, null)?.use {
            if (it.count > 0) {
                it.moveToFirst()
                number =  it.getString(0)
            }
        }
        return  number
    }






    private val cameraPermissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(), ::onGotPermissionCamera)

    private fun onGotPermissionCamera(granted: Boolean) {
        if (granted) permissionCameraGranted()
        else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            } else askUserForOpeningCameraSettings()
        }
    }

    private fun permissionCameraGranted() {
        pickCamera.launch(photoUri)
    }

    private fun askUserForOpeningCameraSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("com.bignerdranch.android.criminalintent", "MainActivity", "CrimeFragment")
        )
        if (requireActivity().packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            Toast.makeText(requireContext(), "Permissions are denied forever", Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(context).setTitle("Permission denied").setMessage("You have denied permissions forever. " +
                    "You can change your decision in app settings\n\n\"" +
                    "Would you like to open app settings?").setPositiveButton("Open") {_, _ ->
                startActivity(appSettingsIntent)
            }.create().show()
        }
    }

    private val pickCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { _ ->
//        if (contactUri != null) {
//            photoView.setImageURI(photoUri)
//            updatePhotoView()
//        }
    }







//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        when {
//            resultCode != Activity.RESULT_OK -> return
//            requestCode == REQUEST_CONTACT && data != null -> {
//                val contactUri: Uri? = data.data
//                val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
//                val cursor = contactUri?.let {
//                    requireActivity().contentResolver.query(
//                        it,
//                        queryFields,
//                        null,
//                        null,
//                        null
//                    )
//                }
//                cursor?.use {
//                    if (it.count == 0) return
//                    it.moveToFirst()
//                    val suspect = it.getString(0)
//                    crime.suspect = suspect
//                    crimeDetailViewModel.saveCrime(crime)
//                    suspectButton.text = suspect
//                }
//            }
//
//            requestCode == REQUEST_PHOTO -> {
//                requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//                updatePhotoView()
//            }
//        }
//    }


    override fun onStop() {
        super.onStop()

        crimeDetailViewModel.saveCrime(crime)
        Log.d(TAG, crime.title)
    }

//    override fun onDetach() {
//        super.onDetach()
//        requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//    }

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = DateFormat.getDateFormat(context).format(crime.date)
        timeButton.text = DateFormat.format("kk:mm:ss", crime.date)
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }

        if (crime.suspect.isNotEmpty()) suspectButton.text = crime.suspect
        callButton.text = if (crime.suspectPhoneNumber == "") "Call to suspect" else crime.suspectPhoneNumber

        updatePhotoView()
    }

    private fun updatePhotoView() {
        if(photoFile.exists()) {
            photoView.isEnabled = true
            val bitmap = getScaleBitmap(photoFile.path, requireActivity())
            photoView.setImageBitmap(bitmap)
            photoView.announceForAccessibility(getText(R.string.image_changed))
            photoView.contentDescription = getString(R.string.crime_photo_image_description)
        } else {
            photoView.setImageDrawable(null)
            photoView.contentDescription = getString(R.string.crime_photo_no_image_description)
        }
    }

    private fun getCrimeReport() :String {
        val solvedString = if (crime.isSolved) getString(R.string.crime_report_solved) else getString(R.string.crime_report_unsolved)
        val dataString = DateFormat.format(DATA_FORMAT, crime.date)
        val suspect = if (crime.suspect.isBlank()) getString(R.string.crime_report_no_suspect) else getString(R.string.crime_report_suspect, crime.suspect)
        return getString(R.string.crime_report, crime.title, dataString, solvedString, suspect)
    }

    companion object {

        //creates new CrimeFragment object
        fun newInstance(crimeId: UUID): CrimeFragment {

            /**
             * @param arg - the argument's package
             * @param Bundle() - package
             */
            //adding the data in fragment arguments
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }

            //joins arguments to fragment and returns the CrimeFragment's object
            return CrimeFragment().apply { arguments = args }
        }
    }
}