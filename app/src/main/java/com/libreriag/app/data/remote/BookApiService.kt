package com.libreriag.app.data.remote

import com.libreriag.app.model.Book
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT // <--- Importar PUT
import retrofit2.http.Path

interface BookApiService {
    @GET("books")
    suspend fun getBooks(): List<Book>

    @POST("books")
    suspend fun createBook(@Body book: Book): Book

    @DELETE("books/{id}")
    suspend fun deleteBook(@Path("id") id: Int): Book

    // --- NUEVO: EDITAR ---
    @PUT("books/{id}")
    suspend fun updateBook(@Path("id") id: Int, @Body book: Book): Book
}