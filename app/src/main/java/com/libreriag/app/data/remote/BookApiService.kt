package com.libreriag.app.data.remote

import com.libreriag.app.model.Book
import com.libreriag.app.model.AuthResponse
import com.libreriag.app.model.LoginRequest
import com.libreriag.app.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BookApiService {
    // --- LIBROS (CRUD) ---
    @GET("books")
    suspend fun getBooks(): List<Book>

    @POST("books")
    suspend fun createBook(@Body book: Book): Book

    @DELETE("books/{id}")
    suspend fun deleteBook(@Path("id") id: Int): Book

    @PUT("books/{id}")
    suspend fun updateBook(@Path("id") id: Int, @Body book: Book): Book

    // --- USUARIOS (Login y Registro) ---
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
}