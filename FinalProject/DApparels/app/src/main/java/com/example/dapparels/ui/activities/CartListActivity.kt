package com.example.dapparels.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dapparels.R
import com.example.dapparels.firestore.FireStoreClass
import com.example.dapparels.models.CartItem
import com.example.dapparels.models.Order
import com.example.dapparels.models.Product
import com.example.dapparels.ui.adapters.CartItemListAdapter
import kotlinx.android.synthetic.main.activity_cart_list.*

/**
 * Cart list activity of the application.
 */
class CartListActivity : BaseActivity(), View.OnClickListener {

    // A global variable for the product list.
    private lateinit var mProductsList: ArrayList<Product>

    // A global variable for the cart list items.
    private lateinit var mCartListItems: ArrayList<CartItem>

    private var mSubTotal : Double = 0.0

    private var mTotalAmount : Double = 0.0

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_cart_list)

        btn_checkout.setOnClickListener(this)

    }

    override fun onResume() {
        super.onResume()

        getProductList()
    }


    private fun getProductList() {

        FireStoreClass().getAllProductsList(this@CartListActivity)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        mProductsList = productsList

        getCartItemsList()
    }

    /**
     * A function to get the list of cart items in the activity.
     */
    private fun getCartItemsList() {

        FireStoreClass().getCartList(this@CartListActivity)
    }

    /**
     * A function to notify the success result of the cart items list from cloud firestore.
     *
     * @param cartList
     */
    fun successCartItemsList(cartList: ArrayList<CartItem>) {


        for (product in mProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {

                    cart.stock_quantity = product.stock_quantity

                    if (product.stock_quantity.toInt() == 0) {
                        cart.cart_quantity = product.stock_quantity
                    }
                }
            }
        }

        mCartListItems = cartList

        if (mCartListItems.size > 0) {

            rv_cart_items_list.visibility = View.VISIBLE
            ll_checkout.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE

            rv_cart_items_list.layoutManager = LinearLayoutManager(this@CartListActivity)
            rv_cart_items_list.setHasFixedSize(true)

            val cartListAdapter = CartItemListAdapter(this@CartListActivity, mCartListItems)
            rv_cart_items_list.adapter = cartListAdapter


            for (item in mCartListItems) {

                val availableQuantity = item.stock_quantity.toInt()

                if (availableQuantity > 0) {
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()

                    mSubTotal += (price * quantity)
                }
            }

            tv_sub_total.text = "$$mSubTotal"
            tv_shipping_charge.text = "$10.0"

            if (mSubTotal > 0) {
                ll_checkout.visibility = View.VISIBLE

                mTotalAmount  = mSubTotal + 10
                tv_total_amount.text = "$$mTotalAmount"
            } else {
                ll_checkout.visibility = View.GONE
            }

        } else {
            rv_cart_items_list.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }

    }

    /**
     * A function to notify the user about the item removed from the cart list.
     */
    fun itemRemovedSuccess() {


        Toast.makeText(
            this@CartListActivity,
            resources.getString(R.string.msg_item_removed_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getCartItemsList()
    }


    fun itemUpdateSuccess() {

        getCartItemsList()
    }

    override fun onClick(v: View?) {

        if(v != null) {
            when(v.id){
               R.id.btn_checkout ->{
                   placeAnOrder()
                   startActivity(Intent(this, DashBoardActivity::class.java))
                   Toast.makeText(this, "Order has been Placed", Toast.LENGTH_LONG).show()
                   finish()


               }
            }
        }
    }


    private fun placeAnOrder(){

        val order = Order(
            FireStoreClass().getCurrentUserID(),
            mCartListItems,
            "My order ${System.currentTimeMillis()}",
            mCartListItems[0].image,
            mSubTotal.toString(),
            "10.0", // The Shipping Charge is fixed as $10 for now in our case.
            mTotalAmount.toString()
        )
        FireStoreClass().placeOrder(this, order)
        FireStoreClass().updateAllDetails(this, mCartListItems)
    }
}