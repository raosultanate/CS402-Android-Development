package com.example.dapparels.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import com.example.dapparels.R
import com.example.dapparels.Utilities.Constants
import com.example.dapparels.firestore.FireStoreClass
import com.example.dapparels.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        tv_forgot_password.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)

        }
        main_login_button.setOnClickListener{
            loginRegisteredUser()

        }
        tv_register.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }


    fun userLoggedInSuccess(user: User){

        //if the user has completed the activity send him to mainActivity page else send the user to the complete profile page.

        if(user.profileCompleted ==  0){
            val intent: Intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        }
        else {
            startActivity(Intent(this, DashBoardActivity::class.java))
        }

        finish()

    }


    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_email_login.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                print("you")
                false
            }

            TextUtils.isEmpty(et_password_login.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }

    }

    private fun loginRegisteredUser() {
        if(validateLoginDetails()) {
            val email: String = et_email_login.text.toString().trim{it <= ' '}
            val password: String = et_password_login.text.toString().trim{it <= ' '}

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
                if(task.isSuccessful){

                    //takes us to the MainActivity.
                    FireStoreClass().getUsersDetails(this)
                    Log.d("BSU", "success")
                    }
                else {
                    showErrorSnackBar(task.exception!!.message.toString(), true)
                    Log.d("BSU", "Failed")
                }

            }
        }
    }

}