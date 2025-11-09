package com.libreriag.app.viewmodel

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.libreriag.app.data.local.BookDatabase
import com.libreriag.app.data.repository.BookRepository
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

    val title = MutableStateFlow("")
    val author = MutableStateFlow("")
    var titleError = MutableStateFlow<String?>(null)
    var authorError = MutableStateFlow<String?>(null)

    fun validateForm(title: String, author: String): Boolean {
        var isValid = true

        if (title.isBlank()) {
            titleError.value = "El título es obligatorio"
            isValid = false
        } else if (title.length < 3) {
            titleError.value = "Debe tener mínimo 3 caracteres"
            isValid = false
        } else {
            titleError.value = null
        }

        if (author.isBlank()) {
            authorError.value = "El autor es obligatorio"
            isValid = false
        } else {
            authorError.value = null
        }

        return isValid
    }


    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> = _photoUri

    fun setPhoto(uri: Uri) {
        _photoUri.value = uri
    }

    fun onTitleChange(v: String) {
        title.value = v
        titleError.value = if (v.isBlank()) "Requerido" else null
    }

    fun onAuthorChange(v: String) {
        author.value = v
        authorError.value = if (v.isBlank()) "Requerido" else null
    }

    /** URI para CameraX */
    fun createImageUri(): Uri {
        val context = getApplication<Application>()
        val imageFile = File(context.filesDir, "photo_${System.currentTimeMillis()}.jpg")

        return FileProvider.getUriForFile(
            context,
            "com.libreriag.app.fileprovider",
            imageFile
        )
    }

    /** Notificación Push */
    private fun showNotification(title: String) {
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
            .setContentTitle("Libro guardado")
            .setContentText("Se agregó: $title")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        NotificationManagerCompat.from(app).notify(1, builder.build())
    }

    fun save() {
        val t = title.value.trim()
        val a = author.value.trim()

        titleError.value = if (t.isBlank()) "Requerido" else null
        authorError.value = if (a.isBlank()) "Requerido" else null

        if (t.isBlank() || a.isBlank()) return

        viewModelScope.launch {
            repo.add(
                Book(
                    title = t,
                    author = a,
                    photo = photoUri.value?.toString()
                )
            )

            showNotification(t) // 🔔 Notificación

            title.value = ""
            author.value = ""
            _photoUri.value = null
        }
    }
}
