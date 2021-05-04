package com.example.dapparels.models

import java.io.Serializable


data class Order (
    val user_id: String = "",
    val items: ArrayList<CartItem> = ArrayList(),
    val title: String = "",
    val image: String = "",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    var id: String = ""
): Serializable {}

