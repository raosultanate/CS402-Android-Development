package com.example.dapparels.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dapparels.R
import com.example.dapparels.firestore.FireStoreClass
import com.example.dapparels.models.Order
import com.example.dapparels.ui.adapters.MyOrdersListAdapter
import com.example.dapparels.ui.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_orders.*

// TODO Step 1: Replace the Fragment with BaseFragment.
class OrdersFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_orders, container, false)
        return root
    }

    override fun onResume() {
        super.onResume()

        getMyOrdersList()
    }

    /**
     * A function to get the list of my orders.
     */
    private fun getMyOrdersList() {

        FireStoreClass().getMyOrdersList(this@OrdersFragment)
    }

    /**
     * A function to get the success result of the my order list from cloud firestore.
     *
     * @param ordersList List of my orders.
     */
    fun populateOrdersListInUI(ordersList: ArrayList<Order>) {

        if (ordersList.size > 0) {

            rv_my_order_items.visibility = View.VISIBLE
            tv_no_orders_found.visibility = View.GONE

            rv_my_order_items.layoutManager = LinearLayoutManager(activity)
            rv_my_order_items.setHasFixedSize(true)

            val myOrdersAdapter = MyOrdersListAdapter(requireActivity(), ordersList)
            rv_my_order_items.adapter = myOrdersAdapter
        } else {
            rv_my_order_items.visibility = View.GONE
            tv_no_orders_found.visibility = View.VISIBLE
        }

    }

}