package com.example.hw_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class InfoDisplay : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_display)

        val studentData: studentData =  intent.getSerializableExtra("student") as studentData

        val nameView: TextView = findViewById(R.id.displayTotalInfo)

        nameView.text = "${studentData.firstName} ${studentData.lastName}"

        val act3: Button = findViewById(R.id.Act3)
       act3.setOnClickListener {
           var intent: Intent = Intent(this, Act3::class.java)
           startActivity(intent)
       }


    }
}