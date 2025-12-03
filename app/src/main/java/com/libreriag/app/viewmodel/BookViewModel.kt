package com.libreriag.app.viewmodel

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.libreriag.app.data.local.BookDatabase
import com.libreriag.app.data.repository.BookRepository
import com.libreriag.app.data.remote.RetrofitClient
import com.libreriag.app.data.remote.ExternalRetrofitClient
import com.libreriag.app.data.remote.ITBook
import com.libreriag.app.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

class BookViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = BookRepository(BookDatabase.get(app).bookDao())

    val books: StateFlow<List<Book>> =
        repo.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Lista de API Externa
    private val _recommendedBooks = MutableStateFlow<List<ITBook>>(emptyList())
    val recommendedBooks: StateFlow<List<ITBook>> = _recommendedBooks

    // --- VARIABLES DEL FORMULARIO ---
    // 'currentId' nos dice si es nuevo (0) o edición (>0)
    var currentId = MutableStateFlow<Int>(0)
    val title = MutableStateFlow("")
    val author = MutableStateFlow("")
    var titleError = MutableStateFlow<String?>(null)
    var authorError = MutableStateFlow<String?>(null)

    init {
        sincronizarConNube()
        cargarRecomendaciones()
    }

    // --- FUNCIONES PARA EDITAR ---

    // Llama a esto cuando toques un libro en la lista para editarlo
    fun prepareUpdate(book: Book) {
        currentId.value = book.id
        title.value = book.title
        author.value = book.author
        if (book.photo != null) _photoUri.value = Uri.parse(book.photo)
    }

    // Limpia el formulario para empezar de cero
    fun clearForm() {
        currentId.value = 0
        title.value = ""
        author.value = ""
        _photoUri.value = null
        titleError.value = null
        authorError.value = null
    }

    // --- GUARDAR (Crear o Editar) ---
    fun save() {
        val t = title.value.trim()
        val a = author.value.trim()

        if (!validateForm(t, a)) return

        viewModelScope.launch {
            val libroParaGuardar = Book(
                id = currentId.value, // Usamos el ID actual (0 o existente)
                title = t,
                author = a,
                photo = photoUri.value?.toString()
            )

            // 1. Guardar Local (Room maneja el ID si es 0, o actualiza si existe)
            repo.add(libroParaGuardar)

            // 2. Guardar Nube (Decidir si es POST o PUT)
            try {
                if (currentId.value == 0) {
                    Log.d("SYNC", "📤 Creando libro nuevo...")
                    RetrofitClient.api.createBook(libroParaGuardar)
                } else {
                    Log.d("SYNC", "📝 Actualizando libro ID ${currentId.value}...")
                    // Llama al PUT que definimos en BookApiService
                    RetrofitClient.api.updateBook(currentId.value, libroParaGuardar)
                }

                Log.d("SYNC", "✅ Operación en nube exitosa")
                sincronizarConNube() // Refrescar para asegurar consistencia
            } catch (e: Exception) {
                Log.e("SYNC", "⚠️ Falló la nube: ${e.message}")
            }

            showNotification(if (currentId.value == 0) "Libro creado" else "Libro actualizado")

            // Limpiamos todo al terminar
            clearForm()
        }
    }

    // --- SINCRONIZACIÓN Y OTROS ---

    fun sincronizarConNube() {
        viewModelScope.launch {
            try {
                val librosNube = RetrofitClient.api.getBooks()
                repo.deleteAll() // Espejo: borrar local
                librosNube.forEach { repo.add(it) }
            } catch (e: Exception) {
                Log.e("SYNC", "Error AWS: ${e.message}")
            }
        }
    }

    fun cargarRecomendaciones() {
        viewModelScope.launch {
            try {
                val response = ExternalRetrofitClient.api.getNewBooks()
                _recommendedBooks.value = response.books
            } catch (e: Exception) {
                Log.e("API_EXTERNA", "Error: ${e.message}")
            }
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repo.delete(book)
            try {
                if (book.id > 0) {
                    RetrofitClient.api.deleteBook(book.id)
                    Log.d("SYNC", "🗑️ Libro eliminado de la nube")
                }
            } catch (e: Exception) {
                Log.e("SYNC", "Error delete: ${e.message}")
            }
        }
    }

    // --- VALIDACIONES Y HELPERS ---

    fun validateForm(title: String, author: String): Boolean {
        var isValid = true
        if (title.isBlank()) {
            titleError.value = "Requerido"
            isValid = false
        } else {
            titleError.value = null
        }
        if (author.isBlank()) {
            authorError.value = "Requerido"
            isValid = false
        } else {
            authorError.value = null
        }
        return isValid
    }

    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> = _photoUri

    fun setPhoto(uri: Uri) { _photoUri.value = uri }

    fun onTitleChange(v: String) {
        title.value = v
        if(v.isNotBlank()) titleError.value = null
    }

    fun onAuthorChange(v: String) {
        author.value = v
        if(v.isNotBlank()) authorError.value = null
    }

    fun createImageUri(): Uri {
        val context = getApplication<Application>()
        val imageFile = File(context.filesDir, "photo_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            context,
            "com.libreriag.app.fileprovider",
            imageFile
        )
    }

    private fun showNotification(msg: String) {
        val app = getApplication<Application>()
        val channelId = "books_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Libros",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = app.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(app, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_save)
            .setContentTitle("Librería G")
            .setContentText(msg)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        NotificationManagerCompat.from(app).notify(1, builder.build())
    }
}