package com.libreriag.app.data.local

import androidx.room.*
import com.libreriag.app.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: Book)

    @Query("SELECT * FROM books ORDER BY id DESC")
    fun getAll(): Flow<List<Book>>
}
