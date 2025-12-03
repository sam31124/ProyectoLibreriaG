package com.libreriag.app.data.local

import androidx.room.*
// Asegúrate que este import sea el correcto según tu proyecto
import com.libreriag.app.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: Book)

    @Query("SELECT * FROM books ORDER BY id DESC")
    fun getAll(): Flow<List<Book>>

    @Delete
    suspend fun delete(book: Book)

    // --- NUEVO: ESTO ES LO QUE FALTABA ---
    @Query("DELETE FROM books")
    suspend fun deleteAll()
}