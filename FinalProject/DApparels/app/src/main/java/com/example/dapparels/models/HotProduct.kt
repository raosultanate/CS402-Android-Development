package com.example.dapparels.models

import java.io.Serializable

data class HotProduct(

    var product_id: String = "",
    val image: String = "",
    val title: String = ""

): Serializable {}