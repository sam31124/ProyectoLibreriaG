package com.libreriag.app.data.remote

import com.libreriag.app.model.Book
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BookApiService {
    // GET: Obtener lista de libros
    @GET("books")
    suspend fun getBooks(): List<Book>

    // POST: Crear libro
    @POST("books")
    suspend fun createBook(@Body book: Book): Book
}