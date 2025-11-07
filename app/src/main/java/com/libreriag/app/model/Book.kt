package com.libreriag.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val autor: String,
    val precio: Double,
    val stock: Int,
    val photoUri: String? = null,
    val latitud: Double? = null,
    val longitud: Double? = null
)
