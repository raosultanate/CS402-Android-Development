package com.example.dapparels.Utilities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    const val HOT_PRODUCTS: String = "hot_products"
    const val STOCK_QUANTITY: String = "stock_quantity"
    const val ORDERS: String = "orders"
    val CART_QUANTITY: String = "cart_quantity"
    const val PRODUCT_ID : String = "product_id"
    const val DEFAULT_CART_QUANTITY: String = "1"
    const val CART_ITEMS: String = "cart_items"
    const val EXTRA_PRODUCT_ID: String = "extra_product_id"
    const val PRODUCTS: String = "products"
    const val USER_ID: String = "user_id"
    const val USERS: String = "users"
    const val FIRST_NAME: String = "firstName"
    const val LAST_NAME: String = "lastName"
    //to store very basic data on the device itself we use shared preferences. (key, value pair) storage type
    const val DANNY_APPARELS_PREFERENCES: String = "DannyA"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val IMAGE_REQUEST_CODE = 1
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val MALE: String = "male"
    const val FEMALE: String = "female"

    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"

    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"
    const val IMAGE : String = "image"
    const val COMPLETE_PROFILE = "profileCompleted"



    //for gallery
    fun showImageChooser(activity: Activity){
        //Intent for launching image selection of phone storage
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //launch the image selection of phone storage using the constant code
        activity.startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE)

    }


    fun getFileExtensions(activity: Activity, uri: Uri?): String?{

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))

    }

}