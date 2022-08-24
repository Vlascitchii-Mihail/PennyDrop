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

//формат даты
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

    //the variable shows a photo's location
    private lateinit var photoFile: File

    //the variable shows where to store a photo in the file system
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
        //arguments save after changing orientation
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

        //crimeDetailViewModel.crimeLiveData listener
        crimeDetailViewModel.crimeLiveData.observe(viewLifecycleOwner, Observer { crime -> crime?.let {
            this.crime = crime

            //returns the path of the photo
            photoFile = crimeDetailViewModel.getPhotoFile(crime)

            //File Provider provides a place in the file system using the photo path
            //getUriForFile() - transforms path to Uri, which is seen by camera
            /**
             * @param "com.bignerdranch.android.criminalintent.fileprovider"  - FileProvider.authorities in Manifest
             */
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

        //data button listener
        dateButton.setOnClickListener {

            //getting DatePickerFragment object and transferring a Date
            DatePickerFragment.newInstance(crime.date).apply {

                //showing DataPickerFragment dialog
                /**
                 * @param this@CrimeFragment.childFragmentManager - Вернуть private FragmentManager из CrimeFragment
                 * для размещения и управления фрагментами внутри этого фрагмента.
                 */
                show(this@CrimeFragment.childFragmentManager, DIALOG_DATE)
            }
        }

        timeButton.setOnClickListener {

            //getting DatePickerFragment object and transferring a Date
            TimePickerFragment.newInstanceTime(crime.date).apply {

                //showing DataPickerFragment dialog
                /**
                 * @param this@CrimeFragment.childFragmentManager - Вернуть private FragmentManager из CrimeFragment
                 * для размещения и управления фрагментами внутри этого фрагмента.
                 */
                show(this@CrimeFragment.childFragmentManager, DIALOG_DATE)
            }
        }

        photoView.setOnClickListener {

            //getting PhotoZoomFragment object and transferring data
            PhotoZoomFragment.newInstance(photoFile).apply {

                //showing PhotoZoomFragment dialog
                /**
                 * @param this@CrimeFragment.childFragmentManager - Вернуть private FragmentManager из CrimeFragment
                 * для размещения и управления фрагментами внутри этого фрагмента.
                 */
                show(this@CrimeFragment.childFragmentManager, DIALOG_DATE)
            }
        }

        reportButton.setOnClickListener {

            //implicit intent
            Intent(Intent.ACTION_SEND).apply {
                type = "text/playn"

                //adding report
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())

                //theme's string
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
            }.also { intent ->

                //creating list of applications for choosing
                val chooserIntent = Intent.createChooser(intent, getString(R.string.send_report))

                ////////////////old method
                //starting activity
                startActivity(chooserIntent)
            }
        }

        suspectButton.apply {

            /**
             * @param ACTION_PICK - Выберите элемент из данных, возвращая то, что было выбрано.
             * @param ContactsContract.Contacts.CONTENT_URI - data path
             */
//                val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            setOnClickListener {
                ////////////////old method
                //getting data from some activity
//                startActivityForResult(pickContactIntent, REQUEST_CONTACT)

                //launch(Manifest.permission.READ_CONTACTS) input – the input required to execute an ActivityResultContract.
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
//
//                    /**
//                     * @param Intent.FLAG_GRANT_WRITE_URI_PERMISSION - permission for all the camera's app
//                     */
//                    requireActivity().grantUriPermission(cameraActivity.activityInfo.packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//                }
//
//                startActivityForResult(captureImage, REQUEST_PHOTO)
//            }
//        }

        photoButton.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            //resolveActivity(Intent) - checking if the activity exists, returns ResolveInfo or null
            val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY)

            //if tje camera app doesn't exist, then block the button
            if (resolvedActivity == null) isEnabled = false

            setOnClickListener {

                //requesting the permission
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

    //creating new ActivityResultLauncher
    //asking about permission
    /**
     * @param ActivityResultContracts.RequestPermission() - contract
     * @param ::onGotPermissionCall - reference to function which uses the users choice
     */
    private val callPermissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(), ::onGotPermissionCall)

    private fun onGotPermissionCall(granted: Boolean) {

        //start the activity if permission granted
        if (granted) permissionCallGranted()
        else {

            //shouldShowRequestPermissionRationale() - returns true if user denied permission not in forever
            if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_LONG).show()

                //show dialog with explanation here
            } else askUserForOpeningCallSettings()
        }
    }

    private fun permissionCallGranted() {
//        Toast.makeText(requireContext(), "Contact permission is granted", Toast.LENGTH_SHORT).show()

        /**
         * @param ACTION_DIAL - inputs the phone number in call app and waiting for user's action
         */
        startActivity(Intent(ACTION_DIAL, Uri.parse("tel:" +crime.suspectPhoneNumber)))
    }

    //show dialog with explanation here
    private fun askUserForOpeningCallSettings() {

        //the intent for starting the application's settings
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,

            //reference to our app
            Uri.fromParts("com.bignerdranch.android.criminalintent", "MainActivity", "CrimeFragment")
        )

        //resolveActivity(Intent) - checking if the activity exists, returns ResolveInfo or null
        if (requireActivity().packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            Toast.makeText(requireContext(), "Permissions are denied forever", Toast.LENGTH_SHORT).show()
        } else {

            //showing dialog
            AlertDialog.Builder(context).setTitle("Permission denied").setMessage("You have denied permissions forever. " +
                    "You can change your decision in app settings\n\n\"" +
                    "Would you like to open app settings?")

                //changing positive button to "Open" and opening the settings
                .setPositiveButton("Open") {_, _ ->
                startActivity(appSettingsIntent)
            }.create().show()
        }
    }




    //creating new ActivityResultLauncher
    /**
     * @param ActivityResultContracts.RequestPermission() - contract
     * @param ::onGotPermissionCall - reference on function
     */
    //launching contact app and returning selected element
    private val contactsPermissionRequestLauncher = registerForActivityResult(

        //requesting permission using function onGotPermissionContact
        ActivityResultContracts.RequestPermission(), ::onGotPermissionContact)

    private fun onGotPermissionContact(granted: Boolean) {

        //permission accepted true
        if (granted) permissionContactsGranted()
        else {

            //shouldShowRequestPermissionRationale() - returns true if user denied permission not in forever
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_LONG).show()

                //show dialog with explanation here
            } else askUserForOpeningContactSettings()
        }
    }

    private fun permissionContactsGranted() {
//        Toast.makeText(requireContext(), "Contact permission is granted", Toast.LENGTH_SHORT).show()

        //launching contact app
        pickContact.launch(null)
    }

    private fun askUserForOpeningContactSettings() {

        //the intent for starting the application's settings
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,

            //reference to our app
            Uri.fromParts("com.bignerdranch.android.criminalintent", "MainActivity", "CrimeFragment")
        )

        //checking if the activity exists
        /**
         * @param packageManager - information about all the activities instaled on the phone
         * @param PackageManager.MATCH_DEFAULT_ONLY - Флаг ограничивает поиск activity с флагом CATEGORY_DEFAULT
         */
        //resolveActivity(Intent) - checking if the activity exists, returns ResolveInfo or null
        if (requireActivity().packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            Toast.makeText(requireContext(), "Permissions are denied forever", Toast.LENGTH_SHORT).show()
        } else {

            //showing dialog
            AlertDialog.Builder(context).setTitle("Permission denied").setMessage("You have denied permissions forever. " +
                    "You can change your decision in app settings\n\n\"" +
                    "Would you like to open app settings?")

                //changing positive button to "Open" and opening the settings
                .setPositiveButton("Open") {_, _ ->
                startActivity((appSettingsIntent))
            }.create().show()
        }
    }

    //    val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)

    //launching contact app using contract ActivityResultContracts.PickContact()
    //contacts view as a listeners
    /**
     * @param contactUri - selected contact
     */
    private val pickContact = registerForActivityResult(ActivityResultContracts.PickContact()) { contactUri ->

//        selected contact from contact app
        /**
         * @param contactUri - location of data
         */
        if (contactUri != null) {

            //receiving and using contact's data
            //sending only one contact on which we clicked in contact app
            getContactNameAndID(contactUri)
        }
    }

    //receiving contact's data
    private fun getContactNameAndID(contactUri: Uri) {

        //Указать, для каких полей запрос должен возвращать значения
        //we have only one contact here
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID)

        //Cursor object
        val cursorNameAndId = contactUri.let { uri ->

            //receiving data from contact app database using queryFields array
            //query() - аналогичен запросу SELECT в SQL и используется для получения данных.
            /**
             * @param uri - uri - location of data
             * @param queryFields == null - column from which we choose contact's number
             * @param selectin == null - as WHERE in SQL, selection conditions
             * @param selectionArgs == null - массив аргументов, которые используются в selection.
             *          Каждый знак вопроса в строке selection будет заменен на аргумент из массива selectionArgs.
             * @param sortOrder == null - аналогичен ORDER BY в SQL. Задает порядок, в котором будут возвращены результаты запроса.
             */
            //so we have only one contact, we don't need snt selection argument
            requireActivity().contentResolver.query(uri, queryFields, null, null, null)
        }

        cursorNameAndId?.use {

            //count - returns a size of the Cursor
            if(it.count > 0) {

                //moveToFirst() - moving the cursor on the first string
                it.moveToFirst()

                //transformation the contact's name in the first string in String
                //receiving suspect name
                val suspect = it.getString(0)
                crime.suspect = suspect
                suspectButton.text = suspect

                //transformation the contact ID in the second string in String
                //phoneNumber() -receiving the suspect's phone number using contact ID
                crime.suspectPhoneNumber = phoneNumber(it.getString(1))
                callButton.text = crime.suspectPhoneNumber

                //saving data in our database
                crimeDetailViewModel.saveCrime(crime)
            }
        }
    }

    //take contact ID and receive the phone number
    private fun phoneNumber(contactId: String): String {
        var number = ""

        //location of data
        val phoneURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        //returns array of contacts' numbers as a column
        val phoneNumberQueryFields = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)

        //query - return ID where ContactsContract.CommonDataKinds.Phone.CONTACT_ID == contactId
        val selectCondition = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"

        //list of contactId
        val phoneQueryParameters = listOf(contactId).toTypedArray()

        //query() - аналогичен запросу SELECT в SQL и используется для получения данных.
        /**
         * @param phoneURI - uri - location of data
         * @param phoneNumberQueryFields - column from which we choose contact's number
         * @param selectCondition - as WHERE in SQL, selection conditions
         * @param phoneQueryParameters - массив аргументов, которые используются в selection.
         *          Каждый знак вопроса в строке selection будет заменен на аргумент из массива selectionArgs.
         * @param null - аналогичен ORDER BY в SQL. Задает порядок, в котором будут возвращены результаты запроса.
         */
        requireActivity().contentResolver.query(phoneURI, phoneNumberQueryFields, selectCondition, phoneQueryParameters, null)?.use {

            //count - returns a size of the Cursor
            if (it.count > 0) {

                //moveToFirst() - moving the cursor on the first string
                it.moveToFirst()
                number =  it.getString(0)
            }
        }
        return  number
    }





    //creating new ActivityResultLauncher
    //asking about permission
    /**
     * @param ActivityResultContracts.RequestPermission() - contract
     * @param ::onGotPermissionCamera - reference to function which uses the users choice
     */
    private val cameraPermissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(), ::onGotPermissionCamera)

    private fun onGotPermissionCamera(granted: Boolean) {

        //start the activity if permission granted
        if (granted) permissionCameraGranted()
        else {

            //shouldShowRequestPermissionRationale() - returns true if user denied permission not in forever
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()

            //show dialog with explanation here
            } else askUserForOpeningCameraSettings()
        }
    }

    private fun permissionCameraGranted() {
        //launching the camera
        pickCamera.launch(photoUri)
    }

    //show dialog with explanation here
    private fun askUserForOpeningCameraSettings() {

        //the intent for starting the application's settings
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,

            //reference to our app
            Uri.fromParts("com.bignerdranch.android.criminalintent", "MainActivity", "CrimeFragment")
        )

        //resolveActivity(Intent) - checking if the activity exists, returns ResolveInfo or null
        if (requireActivity().packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            Toast.makeText(requireContext(), "Permissions are denied forever", Toast.LENGTH_SHORT).show()
        } else {

            //showing dialog
            AlertDialog.Builder(context).setTitle("Permission denied").setMessage("You have denied permissions forever. " +
                    "You can change your decision in app settings\n\n\"" +
                    "Would you like to open app settings?")

                //changing positive button to "Open" and opening the settings
                .setPositiveButton("Open") {_, _ ->
                startActivity(appSettingsIntent)
            }.create().show()
        }
    }

    //doing the photo
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

        //saving crime
        crimeDetailViewModel.saveCrime(crime)
        Log.d(TAG, crime.title)
    }

//    override fun onDetach() {
//        super.onDetach()
//        requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//    }

//    getting data from the DatePickerFragment using the Callbacks
//    override fun onDateSelected(date: Date) {
//        crime.date = date
//        updateUI()
//    }

    //UI refreshing
    private fun updateUI() {
        titleField.setText(crime.title)

        //data format
        //getDateFormat() - Returns a DateFormat object that can format the date in short form according to the context's locale.
        dateButton.text = DateFormat.getDateFormat(context).format(crime.date)
        timeButton.text = DateFormat.format("kk:mm:ss", crime.date)
        solvedCheckBox.apply {
            isChecked = crime.isSolved

            //Пропуск анимации установки флажка при загрузке фрагмента на экран или повороте экрана
            jumpDrawablesToCurrentState()
        }

        //setting the name of the suspect in button.text
        if (crime.suspect.isNotEmpty()) suspectButton.text = crime.suspect
        callButton.text = if (crime.suspectPhoneNumber == "") "Call to suspect" else crime.suspectPhoneNumber

        updatePhotoView()
    }

    //uploading the Bitmap Object
    private fun updatePhotoView() {
        if(photoFile.exists()) {
            photoView.isEnabled = true
            val bitmap = getScaleBitmap(photoFile.path, requireActivity())

            //setting the photo in ImageView
            photoView.setImageBitmap(bitmap)
            photoView.announceForAccessibility(getText(R.string.image_changed))

            //setting the description to photoView for Talk Back app
            photoView.contentDescription = getString(R.string.crime_photo_image_description)
        } else {
            photoView.setImageDrawable(null)

            //setting the description to photoView for Talk Back app
            photoView.contentDescription = getString(R.string.crime_photo_no_image_description)
        }
    }

    //creating new report
    private fun getCrimeReport() :String {
        val solvedString = if (crime.isSolved) getString(R.string.crime_report_solved) else getString(R.string.crime_report_unsolved)
        val dataString = DateFormat.format(DATA_FORMAT, crime.date)

        //isBlank() - Returns true if this string is empty or consists solely of whitespace characters.
        val suspect = if (crime.suspect.isBlank()) getString(R.string.crime_report_no_suspect) else getString(R.string.crime_report_suspect, crime.suspect)

        //Возвращает локализованную отформатированную строку из таблицы строк
        // по умолчанию пакета приложения, заменяя аргументы формата
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