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
import com.example.p1.POST.POST
import com.example.p1.apiInterface
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_products.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : BaseFragment() {

    private val BASE_URL = "https://api.openweathermap.org/"
    private var TEMP_STRING = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }



    fun successHotProductListFromFirestore(hotProductList:ArrayList<HotProduct>){
        getMyData()

        val slideModels: ArrayList<SlideModel> = ArrayList()

        if(hotProductList.size > 0){

            for ( i in hotProductList) {

                val slideModel : SlideModel = SlideModel(i.image, i.title)
                slideModels.add(slideModel)

            }

            slider.setImageList(slideModels)

        }
       //

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

    private fun getMyData(){
        val retroFitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(
            BASE_URL).build().create(apiInterface::class.java)

        val retroFitData = retroFitBuilder.getData()
        retroFitData.enqueue(object : Callback<POST?> {
            override fun onFailure(call: Call<POST?>, t: Throwable) {
                Log.d("P1_Error", "Error: " + t.message)

            }

            override fun onResponse(call: Call<POST?>, response: Response<POST?>) {
                var responseBody = response.body()?.main?.temp!!
                println("Temperature is: " + responseBody.toString())
                TEMP_STRING = toFarenheitConvertor(responseBody.toString())
                tempTV.text = "Today's Temperature: ${TEMP_STRING} \u2109"
            }
        })
    }
}