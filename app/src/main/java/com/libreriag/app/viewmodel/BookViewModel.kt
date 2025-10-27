package com.libreriag.app.viewmodel

import android.app.Application
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

class BookViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = BookRepository(BookDatabase.get(app).bookDao())

    val books: StateFlow<List<Book>> =
        repo.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val title = MutableStateFlow("")
    val author = MutableStateFlow("")
    val titleError = MutableStateFlow<String?>(null)
    val authorError = MutableStateFlow<String?>(null)

    fun onTitleChange(v: String) { title.value = v; if (v.isBlank()) titleError.value = "Requerido" else titleError.value = null }
    fun onAuthorChange(v: String) { author.value = v; if (v.isBlank()) authorError.value = "Requerido" else authorError.value = null }

    fun save() {
        val t = title.value.trim()
        val a = author.value.trim()
        titleError.value = if (t.isBlank()) "Requerido" else null
        authorError.value = if (a.isBlank()) "Requerido" else null
        if (t.isBlank() || a.isBlank()) return

        viewModelScope.launch {
            repo.add(Book(title = t, author = a))
            title.value = ""
            author.value = ""
        }
    }
}
