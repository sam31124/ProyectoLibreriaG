package com.libreriag.app.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// 1. MODELOS DE DATOS (Cómo viene el JSON de la API externa)
data class ITBookResponse(
    @SerializedName("books") val books: List<ITBook>
)

data class ITBook(
    @SerializedName("title") val title: String,
    @SerializedName("subtitle") val subtitle: String,
    @SerializedName("image") val imageUrl: String,
    @SerializedName("isbn13") val isbn: String
)

// 2. INTERFAZ (El Endpoint)
interface ExternalApiService {
    @GET("new") // Ruta: https://api.itbook.store/1.0/new
    suspend fun getNewBooks(): ITBookResponse
}

// 3. CLIENTE RETROFIT (Exclusivo para esta API)
object ExternalRetrofitClient {
    private const val BASE_URL = "https://api.itbook.store/1.0/"

    val api: ExternalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExternalApiService::class.java)
    }
}