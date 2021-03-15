package com.example.p1

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup

class activity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity3)

        val radioGroup = findViewById<RadioGroup>(R.id.actThreeRadioGroup)
        val imageViewActThree = findViewById<ImageView>(R.id.imageButtonActThree)

        imageViewActThree.setImageResource(R.drawable.meme1)

        radioGroup.setOnCheckedChangeListener { group, i ->
            var rb = findViewById<RadioButton>(i)
            if(rb.id == R.id.meme1){
                Log.d("hey","I am here")
                imageViewActThree.setImageResource(R.drawable.meme1)

            }
            else if (rb.id == R.id.meme2){
                imageViewActThree.setImageResource(R.drawable.meme2)
            }
            else {
                imageViewActThree.setImageResource(R.drawable.meme3)
            }

        }

    }
}