package com.example.p1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val launchActivityTwoBtn: Button = findViewById<Button>(R.id.actTwoButton)

        launchActivityTwoBtn.setOnClickListener {
            Intent(this, activity2::class.java).also{
                startActivity(it)
            }

        }
    }

}