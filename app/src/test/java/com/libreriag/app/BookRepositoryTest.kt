package com.libreriag.app

import com.libreriag.app.data.local.BookDao
import com.libreriag.app.data.repository.BookRepository
import com.libreriag.app.model.Book
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class BookRepositoryTest {

    private val mockDao = mockk<BookDao>(relaxed = true)
    private val repository = BookRepository(mockDao)

    // TEST 1: Insertar
    @Test
    fun `add debe llamar al dao insert`() = runTest {
        val libro = Book(id = 1, title = "Test", author = "Yo")
        repository.add(libro)
        coVerify { mockDao.insert(libro) }
    }

    // TEST 2: Borrar Todo
    @Test
    fun `deleteAll debe llamar al dao deleteAll`() = runTest {
        repository.deleteAll()
        coVerify { mockDao.deleteAll() }
    }

    // TEST 3: Borrar Uno (NUEVO)
    @Test
    fun `delete debe llamar al dao delete con el libro correcto`() = runTest {
        val libro = Book(id = 2, title = "A Borrar", author = "Nadie")
        repository.delete(libro)
        coVerify { mockDao.delete(libro) }
    }

    // TEST 4: Obtener Todos (NUEVO)
    @Test
    fun `getAll debe devolver el flujo de datos del dao`() = runTest {
        // GIVEN (Dado que el DAO devuelve una lista con 1 libro)
        val listaFicticia = listOf(Book(1, "A", "B"))
        coEvery { mockDao.getAll() } returns flowOf(listaFicticia)

        // WHEN (Cuando pedimos los datos al repo)
        val resultado = repository.getAll()

        // THEN (Verificamos que el resultado sea igual a lo que dio el DAO)
        resultado.collect { libros ->
            assertEquals(1, libros.size)
            assertEquals("A", libros[0].title)
        }
    }
}