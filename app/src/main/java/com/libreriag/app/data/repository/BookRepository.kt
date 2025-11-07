package com.libreriag.app.data.repository

import com.libreriag.app.data.local.BookDao
import com.libreriag.app.model.Book

class BookRepository(private val dao: BookDao) {

    val libros = dao.getAllBooks()

    suspend fun agregarLibro(book: Book) {
        dao.insertBook(book)
    }

    suspend fun eliminarLibro(book: Book) {
        dao.deleteBook(book)
    }

    suspend fun actualizarLibro(book: Book) {
        dao.updateBook(book)
    }
}
