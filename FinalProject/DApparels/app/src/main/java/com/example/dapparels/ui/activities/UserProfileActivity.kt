package com.example.dapparels.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dapparels.R
import com.example.dapparels.Utilities.Constants
import com.example.dapparels.Utilities.GlideLoader
import com.example.dapparels.firestore.FireStoreClass
import com.example.dapparels.models.User
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {


    private lateinit var userProfileData: User
    private var mselectedImageFileUri: Uri? = null
    private var mUserProfileImageUrL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        userProfileData = intent.getSerializableExtra(Constants.EXTRA_USER_DETAILS) as User

        if (userProfileData != null) {

            completeProfile_FirstName_TextView.isEnabled = false
            completeProfile_FirstName_TextView.setText(userProfileData.firstName)
            completeProfile_LastName_TextView.isEnabled = false
            completeProfile_LastName_TextView.setText(userProfileData.lastName)
            completeProfile_EmailID_TextView.isEnabled = false
            completeProfile_EmailID_TextView.setText(userProfileData.email)

            userPhoto.setOnClickListener(this@UserProfileActivity)
            saveButton.setOnClickListener(this@UserProfileActivity)

        }

    }

    override fun onClick(v: View?) {
        if (v != null) {

            when (v.id) {
                R.id.userPhoto -> {

                    //Here I will check for permission and build conditions accordingly.

                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        Constants.showImageChooser(this)
                    } else {
                        //If permission not given.
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }

                }
                R.id.saveButton -> {
                    if(validateUserProfileDetails()){
                        //do something here
                        //Must store key value pair to firestore database. This will facilitate to do the job


                        if (mselectedImageFileUri != null) {
                            FireStoreClass().uploadImageToCloudStorage(this, mselectedImageFileUri)
                        }

                        else {
                            updateUserProfileDetails()
                        }


                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {

            //If Permission granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Constants.showImageChooser(this)

            } else {
                //Display toast if permission is not granted.
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Constants.IMAGE_REQUEST_CODE){
                if(data != null){
                    try {
                        //get the path of the image from the phone storage
                        mselectedImageFileUri = data.data!!
                        //set the imageview with the image path
                        GlideLoader(this).loadUserPicture(mselectedImageFileUri!!, userPhoto)
                    }

                    catch (e : IOException){
                        e.printStackTrace()
                        Toast.makeText(this, resources.getString(R.string.imageLoad_Failed), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }



    private fun validateUserProfileDetails():Boolean {
        return when {
            TextUtils.isEmpty(completeProfile_MobileNumber_TextView.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun userProfileUpdateSuccess(){
        Toast.makeText(this, resources.getString(R.string.update_success), Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL: String) {
        //if the image was uploaded successfully than.
        mUserProfileImageUrL = imageURL
        updateUserProfileDetails()

    }

    private fun updateUserProfileDetails(){

        val userHashMap = HashMap<String, Any>()
        //get mobile number from the edittextview
        val mobileNumber = completeProfile_MobileNumber_TextView.text.toString().trim{it <= ' '}

        //getting gender selection from the radio group
        val gender = if(radioButtonMale.isChecked){
            Constants.MALE
        }
        else {
            Constants.FEMALE
        }

        userHashMap[Constants.GENDER] = gender


        //make sure that the profileimage is not empty
        if(mUserProfileImageUrL.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageUrL
        }

        //make sure mobile number is not empty

        if(mobileNumber.isNotEmpty()){
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        //profile has been completed!
        userHashMap[Constants.COMPLETE_PROFILE] = 1


        //Now that the hashmap has been constructed time to push it to firestore.
        FireStoreClass().updateUserProfileData(this, userHashMap)
    }
}
