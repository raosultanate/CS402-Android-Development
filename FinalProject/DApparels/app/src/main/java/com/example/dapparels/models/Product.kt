package com.example.dapparels.models

import java.io.Serializable

class Product(

    val description: String = "",
    var product_id: String = "",
    val image: String = "",
    val price: String = "",
    val stock_quantity: String = "",
    val title: String = ""
): Serializable {}