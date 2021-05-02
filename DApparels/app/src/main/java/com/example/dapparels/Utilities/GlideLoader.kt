package com.example.dapparels.Utilities

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.dapparels.R
import java.io.IOException


class GlideLoader(val context: Context) {

    fun loadUserPicture(imageUri: String, imageView: ImageView){
        try {
            //Load the user image in the ImageView
            Glide.with(context).load(imageUri).centerCrop().placeholder(R.drawable.image_placeholder).into(imageView)
        }

        catch (e: IOException){
            e.printStackTrace()
        }
    }
    fun loadProductPicture(imageUri: String, imageView: ImageView){
        try {
            //Load the user image in the ImageView
            Glide.with(context).load(imageUri).centerCrop().into(imageView)
        }

        catch (e: IOException){
            e.printStackTrace()
        }
    }
}