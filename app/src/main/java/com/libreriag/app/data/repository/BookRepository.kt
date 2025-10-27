package com.libreriag.app.data.repository

import com.libreriag.app.data.local.BookDao
import com.libreriag.app.model.Book

class BookRepository(private val dao: BookDao) {
    fun getAll() = dao.getAll()
    suspend fun add(book: Book) = dao.insert(book)
}
