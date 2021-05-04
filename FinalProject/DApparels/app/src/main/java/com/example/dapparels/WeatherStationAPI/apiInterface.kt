package com.example.p1

import com.example.p1.POST.POST
import retrofit2.Call
import retrofit2.http.GET

interface apiInterface {
    @GET("data/2.5/weather?q=Boise&appid=ff5cde9a98fa8bacf1cd5ec9fc885680")
    fun getData(): Call<POST>
}