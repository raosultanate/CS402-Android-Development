package com.example.dapparels.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.example.dapparels.R
import com.example.dapparels.firestore.FireStoreClass
import com.example.dapparels.models.HotProduct
import com.example.dapparels.models.Product
import com.example.dapparels.ui.adapters.MyProductListAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_products.*

class HomeFragment : BaseFragment() {

    private val BASE_URL = "https://api.openweathermap.org/"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }



    fun successHotProductListFromFirestore(hotProductList:ArrayList<HotProduct>){


        val slideModels: ArrayList<SlideModel> = ArrayList()

        if(hotProductList.size > 0){

            for ( i in hotProductList) {

                val slideModel : SlideModel = SlideModel(i.image, i.title)
                slideModels.add(slideModel)

            }

            slider.setImageList(slideModels)

        }

    }


    private fun getProductListfromFireStore(){
        FireStoreClass().getHotProductList(this)
    }


    override fun onResume() {
        super.onResume()
        getProductListfromFireStore()
    }

    fun toFarenheitConvertor(numberStringKelvin: String): String {
        val numberDoubleKelvin: Double = numberStringKelvin.toDouble()
        val farenheit: Double = (((numberDoubleKelvin - 273.15)*9)/5) +32
        val farenheitStr = String.format("%.2f", farenheit)
        return farenheitStr

    }

//    private fun getMyData(){
//        val retroFitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(
//            BASE_URL).build().create(apiInterface::class.java)
//
//        val retroFitData = retroFitBuilder.getData()
//
//    }
}