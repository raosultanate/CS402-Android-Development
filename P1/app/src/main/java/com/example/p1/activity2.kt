package com.example.p1

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.p1.MessageEvent.MessageEvent
import com.example.p1.POST.POST
import firstFragment
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val REQUEST_CODE = 42
private const val BASE_URL = "https://api.openweathermap.org/"
lateinit var firstFragment: firstFragment
lateinit var scndFragment: secondFragment
private  var responseBody: Double = 0.0
private lateinit var txtView: TextView
private lateinit var fragTxtView: TextView

class activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity2)

        val launchActivityThreeBtn: Button = findViewById<Button>(R.id.actThreeButton)

        launchActivityThreeBtn.setOnClickListener {
            Intent(this, activity3::class.java).also{
                startActivity(it)
            }

        }


        val cameraButton = findViewById<Button>(R.id.cameraButton)
        firstFragment = firstFragment()
        cameraButton.setOnClickListener {


            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, firstFragment)
                commit()

            }

            val cameraCheckPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

            if (cameraCheckPermission != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                    val builder = AlertDialog.Builder(this)

                    val message = getString(R.string.permission_message)
                    builder.setTitle(R.string.permission_title)
                        .setMessage(message)
                        .setPositiveButton("OK") { _, _ ->
                            requestPermission()
                        }

                    val dialog = builder.create()
                    dialog.show()
                }
                else{
                    Log.d("BSU", "Should not show rationale")
                    requestPermission() // Will not display the request
                }
            }
            else{
                launchCamera()
            }
       }



        val tempButton = findViewById<Button>(R.id.tempButton)
        tempButton.setOnClickListener{
            //clean up the value
            txtView = findViewById<TextView>(R.id.textView)
            txtView.text = ""
            //API call
            getMyData()

            //preparing data to be sent ==> data class already createed in MessageEvent Package
            val newMessageToSend: MessageEvent = MessageEvent(responseBody.toString())

            //send/POST data to EventBus
            EventBus.getDefault().post(newMessageToSend)

            //setup temp Fragment
            scndFragment = secondFragment()
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.flFragment, scndFragment)
//                commit()
//            }


        }


    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 9090)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 123)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for ((index, permission) in permissions.withIndex()){
            if( permission == Manifest.permission.CAMERA){
                if( grantResults[index] == PackageManager.PERMISSION_GRANTED){
                    launchCamera()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( requestCode == 9090){

            if( data != null ) {
                val imageData: Bitmap = data.extras!!.get("data") as Bitmap
                val imageView = firstFragment.view?.findViewById<ImageView>(R.id.pictureHolder)
                imageView?.setImageBitmap(imageData)
                val textView = findViewById<TextView>(R.id.textView)
                textView.text = ""


            }
        }
    }

    private fun getMyData(){
        val retroFitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(
            BASE_URL).build().create(apiInterface::class.java)

        val retroFitData = retroFitBuilder.getData()
        retroFitData.enqueue(object : Callback<POST?> {
            override fun onFailure(call: Call<POST?>, t: Throwable) {
                Log.d("P1_Error", "Error: " + t.message)

            }

            override fun onResponse(call: Call<POST?>, response: Response<POST?>) {
                responseBody = response.body()?.main?.temp!!
                
                supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, scndFragment)
                commit()
            }
               // println("Temperature is: " + responseBody.toString())

            }
        })
    }

}