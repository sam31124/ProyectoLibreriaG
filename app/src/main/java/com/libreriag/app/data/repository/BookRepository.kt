package com.libreriag.app.data.repository

import com.libreriag.app.data.local.BookDao
// Asegúrate de que este import coincida con la ubicación de tu Book.kt
import com.libreriag.app.model.Book

class BookRepository(private val dao: BookDao) {

    // Obtener todos los libros
    fun getAll() = dao.getAll()

    // Insertar libro
    suspend fun add(book: Book) = dao.insert(book)

    // Eliminar un libro específico
    suspend fun delete(book: Book) {
        dao.delete(book)
    }

    // --- 👇 ESTA ES LA FUNCIÓN QUE FALTABA ---
    // Borrar TODA la base de datos local (para la sincronización espejo)
    suspend fun deleteAll() {
        dao.deleteAll()
    }
}