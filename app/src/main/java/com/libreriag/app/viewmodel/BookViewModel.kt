package com.libreriag.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libreriag.app.data.repository.BookRepository
import com.libreriag.app.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel(
    private val repository: BookRepository
) : ViewModel() {

    // Lista de libros desde Room
    val libros: StateFlow<List<Book>> = repository.libros as StateFlow<List<Book>>

    // Mostrar mensajes de éxito
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    //Guardar libro
    fun agregarLibro(book: Book) {
        viewModelScope.launch {
            repository.agregarLibro(book)
            _mensaje.value = "Libro agregado correctamente"
        }
    }

    // Eliminar libro
    fun eliminarLibro(book: Book) {
        viewModelScope.launch {
            repository.eliminarLibro(book)
            _mensaje.value = "Libro eliminado"
        }
    }

    // Actualizar libro
    fun actualizarLibro(book: Book) {
        viewModelScope.launch {
            repository.actualizarLibro(book)
            _mensaje.value = "Libro actualizado"
        }
    }

    // Limpiar mensaje
    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
