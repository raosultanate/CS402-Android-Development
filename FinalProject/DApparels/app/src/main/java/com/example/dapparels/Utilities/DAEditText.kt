package com.example.dapparels.Utilities

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class DAEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs){
    init {
        applyFont()
    }

    private fun applyFont() {
        val typeFace : Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        setTypeface(typeFace)

    }
}