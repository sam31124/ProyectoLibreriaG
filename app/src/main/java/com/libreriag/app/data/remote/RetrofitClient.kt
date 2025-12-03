package com.libreriag.app.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // ⚠️ REEMPLAZA ESTO CON LA IP DE TU SERVIDOR UBUNTU (AWS)
    // Ejemplo: "[http://54.123.45.67:3000/](http://54.123.45.67:3000/)"
    private const val BASE_URL = "http://54.162.164.212:3000/"

    val api: BookApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApiService::class.java)
    }
}