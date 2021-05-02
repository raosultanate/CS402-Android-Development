package com.example.dapparels.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.dapparels.R
import com.example.dapparels.Utilities.Constants
import com.example.dapparels.Utilities.GlideLoader
import com.example.dapparels.firestore.FireStoreClass
import com.example.dapparels.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), View.OnClickListener{

    private lateinit var UserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        btn_logout.setOnClickListener(this)
        tv_edit.setOnClickListener(this)
    }
    //goes to the fireStore class to get the user details.
    private fun getUserDetails(){
        FireStoreClass().getUsersDetails(this)
    }
    //views are set with relevant data
    fun getuserDetailSuccess(user: User) {

        UserDetails = user
        GlideLoader(this).loadUserPicture(user.image, profileSettingUserPicture)
        tv_name.text = "${user.firstName} ${user.lastName}"
        tv_gender.text = user.gender
        tv_email.text = user.email
        tv_mobile_number.text = "${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(view: View?) {

        if(view!=null) {


            when(view.id){
                R.id.btn_logout ->{
                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(this, LoginActivity::class.java)
                    //clears out all layers of activity on the device cache
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

                R.id.tv_edit -> {

                    val intent = Intent(this, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, UserDetails)
                    startActivity(intent)

                }

            }


        }
    }
}