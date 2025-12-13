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
// 👇 Imports de Modelos (Libros y Usuarios)
import com.libreriag.app.model.Book
import com.libreriag.app.model.User
import com.libreriag.app.model.LoginRequest
import com.libreriag.app.model.RegisterRequest
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

    // --- USUARIOS: Login y Registro ---
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    val loginEmail = MutableStateFlow("")
    val loginPassword = MutableStateFlow("")
    val loginError = MutableStateFlow<String?>(null)

    val registerName = MutableStateFlow("")
    val registerEmail = MutableStateFlow("")
    val registerPassword = MutableStateFlow("")
    val registerError = MutableStateFlow<String?>(null)

    // --- VARIABLES DEL FORMULARIO LIBROS ---
    var currentId = MutableStateFlow<Int>(0)
    val title = MutableStateFlow("")
    val author = MutableStateFlow("")
    var titleError = MutableStateFlow<String?>(null)
    var authorError = MutableStateFlow<String?>(null)

    init {
        sincronizarConNube()
        cargarRecomendaciones()
    }

    // --- LOGIN ---
    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            loginError.value = null
            try {
                Log.d("LOGIN", "Intentando entrar con: ${loginEmail.value}")
                val response = RetrofitClient.api.login(
                    LoginRequest(loginEmail.value, loginPassword.value)
                )

                if (response.isSuccessful && response.body()?.user != null) {
                    _currentUser.value = response.body()?.user
                    Log.d("LOGIN", "✅ Éxito! Rol: ${_currentUser.value?.role}")
                    onSuccess()
                } else {
                    loginError.value = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                loginError.value = "Error de conexión: ${e.message}"
            }
        }
    }

    // --- REGISTRO ---
    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            registerError.value = null
            try {
                val response = RetrofitClient.api.register(
                    RegisterRequest(registerName.value, registerEmail.value, registerPassword.value)
                )
                if (response.isSuccessful && response.body()?.user != null) {
                    _currentUser.value = response.body()?.user
                    onSuccess()
                } else {
                    registerError.value = "Error al registrarse (Email duplicado?)"
                }
            } catch (e: Exception) {
                registerError.value = "Error de conexión: ${e.message}"
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        loginEmail.value = ""
        loginPassword.value = ""
    }

    // --- CRUD LIBROS (Crear y Editar) ---
    fun save() {
        val t = title.value.trim()
        val a = author.value.trim()

        if (!validateForm(t, a)) return

        viewModelScope.launch {
            val libroParaGuardar = Book(
                id = currentId.value,
                title = t,
                author = a,
                photo = photoUri.value?.toString()
            )

            // 1. Guardar Local
            repo.add(libroParaGuardar)

            // 2. Guardar Nube
            try {
                if (currentId.value == 0) {
                    Log.d("SYNC", "📤 Creando libro nuevo...")
                    RetrofitClient.api.createBook(libroParaGuardar)
                } else {
                    Log.d("SYNC", "📝 Actualizando libro ID ${currentId.value}...")
                    RetrofitClient.api.updateBook(currentId.value, libroParaGuardar)
                }

                Log.d("SYNC", "✅ Operación en nube exitosa")
                sincronizarConNube()
            } catch (e: Exception) {
                Log.e("SYNC", "⚠️ Falló la nube: ${e.message}")
            }

            showNotification(if (currentId.value == 0) "Libro creado" else "Libro actualizado")
            clearForm()
        }
    }

    // --- FUNCIONES AUXILIARES ---

    fun prepareUpdate(book: Book) {
        currentId.value = book.id
        title.value = book.title
        author.value = book.author
        if (book.photo != null) _photoUri.value = Uri.parse(book.photo)
    }

    fun clearForm() {
        currentId.value = 0
        title.value = ""
        author.value = ""
        _photoUri.value = null
        titleError.value = null
        authorError.value = null
    }

    fun sincronizarConNube() {
        viewModelScope.launch {
            try {
                val librosNube = RetrofitClient.api.getBooks()
                repo.deleteAll() // Espejo
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
                }
            } catch (e: Exception) {
                Log.e("SYNC", "Error delete: ${e.message}")
            }
        }
    }

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