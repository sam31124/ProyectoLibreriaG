package com.libreriag.app.data.local

import androidx.room.*
import com.libreriag.app.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM books ORDER BY titulo ASC")
    fun getAllBooks(): Flow<List<Book>>
}
