package com.libreriag.app.model // <--- ESTO DEBE COINCIDIR CON TU CARPETA

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val photo: String?
)

data class AuthResponse(
    val message: String,
    val user: User?
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)