package com.example.hw_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val recycler_view =  findViewById(R.id.recycler_view) as RecyclerView
        val firstNameTextValue: EditText =  findViewById(R.id.studentFirstName)
        val lastNameTextValue: EditText = findViewById(R.id.studentLastName)
        val radioMale: RadioButton = findViewById(R.id.radioButton3)
        val radioFemale: RadioButton = findViewById(R.id.radioButton4)
        val submitButton: Button = findViewById(R.id.submitButton)



        var person: studentData

        submitButton.setOnClickListener {

            val firstNameString: String = firstNameTextValue.text.toString()
            val lastNameString: String = lastNameTextValue.text.toString()


            if (radioFemale.isChecked) {
                Log.d("dan", "female is checked")

                person = studentData(firstNameString, lastNameString, "Female")
                var intent: Intent = Intent(this, InfoDisplay::class.java)
                intent.putExtra("student", person)
                startActivity(intent)

            } else {

                Log.d("dan", "male is checked")
                person = studentData(firstNameString, lastNameString, "Male")
                var intent: Intent = Intent(this, InfoDisplay::class.java)
                intent.putExtra("student", person)
                startActivity(intent)
            }

        }

    }
}