package com.example.dapparels.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dapparels.R
import com.example.dapparels.firestore.FireStoreClass
import com.example.dapparels.models.Product
import com.example.dapparels.ui.adapters.MyProductListAdapter
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : BaseFragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_products, container, false)

        return root
    }



    fun successProductListFromFirestore(productList:ArrayList<Product>){

        for(i in productList) {
            Log.i("Product Name", i.image)
        }

        if(productList.size > 0){
            rv_my_product_items.visibility = View.VISIBLE
            tv_no_products_found.visibility = View.GONE

            rv_my_product_items.layoutManager = LinearLayoutManager(activity)
            rv_my_product_items.setHasFixedSize(true)
            val adapterProducts = MyProductListAdapter(requireActivity(), productList)
            rv_my_product_items.adapter = adapterProducts
        }

    }


    fun getProductListfromFireStore(){
        FireStoreClass().getProductList(this)
    }


    override fun onResume() {
        super.onResume()
        getProductListfromFireStore()
    }
}