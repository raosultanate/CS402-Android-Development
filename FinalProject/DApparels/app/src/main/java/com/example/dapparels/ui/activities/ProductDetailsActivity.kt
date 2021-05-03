package com.example.dapparels.ui.activities

import android.os.Bundle
import android.util.Log
import com.example.dapparels.R
import com.example.dapparels.Utilities.Constants
import com.example.dapparels.Utilities.GlideLoader
import com.example.dapparels.firestore.FireStoreClass
import com.example.dapparels.models.Product
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity() {

    private var mProductId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)


        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {

            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!

            Log.i("ID_HERE", mProductId)
            FireStoreClass().getProductDetails(this, mProductId)


        } else {

        }

    }

   fun productDetailSuccess(product : Product){
       GlideLoader(this@ProductDetailsActivity).loadProductPicture(product.image, iv_product_detail_image)
       tv_product_details_title.text = product.title
       tv_product_details_price.text = "$${product.price}"
       tv_product_details_description.text = product.description
       tv_product_details_stock_quantity.text = product.stock_quantity

   }

}