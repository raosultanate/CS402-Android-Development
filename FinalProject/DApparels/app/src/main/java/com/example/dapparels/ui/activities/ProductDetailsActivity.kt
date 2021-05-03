package com.example.dapparels.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.dapparels.R
import com.example.dapparels.Utilities.Constants
import com.example.dapparels.Utilities.GlideLoader
import com.example.dapparels.firestore.FireStoreClass
import com.example.dapparels.models.CartItem
import com.example.dapparels.models.Product
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mProductId: String = ""

    private lateinit var mProductDetails: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)


        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {

            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!

            Log.i("ID_HERE", mProductId)
            FireStoreClass().getProductDetails(this, mProductId)

        }

        btn_add_to_cart.setOnClickListener(this)
        btn_go_to_cart.setOnClickListener(this)

    }

   fun productDetailSuccess(product : Product){
       mProductDetails = product
       GlideLoader(this@ProductDetailsActivity).loadProductPicture(product.image, iv_product_detail_image)
       tv_product_details_title.text = product.title
       tv_product_details_price.text = "$${product.price}"
       tv_product_details_description.text = product.description
       tv_product_details_stock_quantity.text = product.stock_quantity

       if(FireStoreClass().getCurrentUserID() == product.product_id){
           //do nothing here
       }

       else {
           FireStoreClass().checkIfItemExitInCart(this, mProductId)
       }


   }

    private fun addToCart(){
        val cartItem = CartItem(FireStoreClass().getCurrentUserID(), mProductId, mProductDetails.title, mProductDetails.price, mProductDetails.image, Constants.DEFAULT_CART_QUANTITY)
        FireStoreClass().addCartItems(this, cartItem)
    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.btn_add_to_cart -> {
                    addToCart()
                }

                R.id.btn_go_to_cart -> {

                }
            }
        }
    }

    fun addToCartSuccess(){
        Toast.makeText(this, resources.getString(R.string.success_message_item_add_to_cart), Toast.LENGTH_SHORT).show()
        btn_add_to_cart.visibility = View.GONE
    }

    fun productExistInCart(){
        btn_add_to_cart.visibility = View.GONE
        btn_go_to_cart.visibility = View.VISIBLE

    }

    fun productDoesNotExistInCart(){
        btn_add_to_cart.visibility = View.VISIBLE
        btn_go_to_cart.visibility = View.VISIBLE
    }

}