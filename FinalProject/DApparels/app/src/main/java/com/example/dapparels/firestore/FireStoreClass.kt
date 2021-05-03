package com.example.dapparels.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.dapparels.ui.activities.LoginActivity
import com.example.dapparels.ui.activities.RegisterActivity
import com.example.dapparels.ui.activities.UserProfileActivity
import com.example.dapparels.Utilities.Constants
import com.example.dapparels.models.Product
import com.example.dapparels.models.User
import com.example.dapparels.ui.activities.SettingsActivity
import com.example.dapparels.ui.fragments.DashboardFragment
import com.example.dapparels.ui.fragments.ProductsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    //setting data here
    fun registerUser(activity: RegisterActivity, userInfo: User){

        //users collection name is created in the firestore, document ID for user fields. Each document is uniquely identified with the user ID. set is used to upload or store data. merge the data instead
        //of replacing fields in it
        mFireStore.collection(Constants.USERS).document(userInfo.id).set(userInfo, SetOptions.merge()).addOnSuccessListener {

            activity.userRegisterationSuccess()

        }.addOnFailureListener{

            Log.d("Danny", "Error occurred while registering the user.")
        }
    }

    //getting users ID of the User that is currently logged in
    fun getCurrentUserID() : String {
        //instance of currentUser
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""

        //check if current user is empty
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    //getting data here
    fun getUsersDetails(activity: Activity) {

        // where Constants.USERS contains the name of the collections
        mFireStore.collection(Constants.USERS).document(getCurrentUserID()).get().addOnSuccessListener { document ->
            //butting data of document into the data structure user class
            val user = document.toObject(User::class.java)!!


            //mode is private here so that the data is only accessible inside our application instead of anywhere else.
            //sharedPreferences is a dataStorage on the device.
            val sharePreferences = activity.getSharedPreferences(Constants.DANNY_APPARELS_PREFERENCES, Context.MODE_PRIVATE)

            val editor: SharedPreferences.Editor = sharePreferences.edit()

            //time to store the data in the shared preference using the editor

            //key: LOGGED_IN_USERNAME
            //value: Daniel Rao
            editor.putString(Constants.LOGGED_IN_USERNAME, "${user.firstName} ${user.lastName}")
            editor.apply()

            when (activity) {
                is LoginActivity -> {
                    //call a function of base Activity for transferring the result to it.
                    activity.userLoggedInSuccess(user)
                }

                is SettingsActivity -> {
                    //sends the data back to the SettingsActivity.
                    activity.getuserDetailSuccess(user)
                }

            }
        }
    }

    //using update method for the document.
    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS).document(getCurrentUserID()).update(userHashMap).addOnSuccessListener{
            when(activity){
                is UserProfileActivity -> {
                    //takes us to the next activity which is the main activity.
                    activity.userProfileUpdateSuccess()
                }
            }
        }.addOnFailureListener{e ->
            Log.e(activity.javaClass.simpleName, "Error updating the user details.", e)
        }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?) {

        //to get storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.
        child(Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "." + Constants.getFileExtensions(activity, imageFileURI))

        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->

            Log.e("Firebase ImageURL",taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                Log.e("Downloadable Image URL", uri.toString())
                when(activity){
                    is UserProfileActivity -> {
                        activity.imageUploadSuccess(uri.toString())
                    }
                }
            }

        }.addOnFailureListener{exception ->
            Log.e(activity.javaClass.simpleName, exception.message, exception)
        }
    }


    //get product list from the firestore
    fun getProductList(fragment: Fragment) {
        mFireStore.collection(Constants.PRODUCTS).get().addOnSuccessListener { megaDocument ->
            val productsList: ArrayList<Product> = ArrayList()

            for(i in megaDocument.documents) {
                val product = i.toObject(Product::class.java)
                product!!.product_id = i.id
                productsList.add(product)
            }

            when(fragment) {
                is ProductsFragment -> {
                    fragment.successProductListFromFirestore(productsList)
                }
            }

        }

    }

    /**
     * A function to get the dashboard items list. The list will be an overall items list, not based on the user's id.
     */
    fun getDashboardItemsList(fragment: DashboardFragment) {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.PRODUCTS)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val productsList: ArrayList<Product> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)!!
                    product.product_id = i.id
                    productsList.add(product)
                }

                // Pass the success result to the base fragment.
                fragment.successDashboardItemsList(productsList)
            }
            .addOnFailureListener { e ->

                Log.e(fragment.javaClass.simpleName, "Error while getting dashboard items list.", e)
            }
    }

}