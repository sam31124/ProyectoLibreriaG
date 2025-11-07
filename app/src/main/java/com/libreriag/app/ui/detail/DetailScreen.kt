package com.libreriag.app.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.libreriag.app.viewmodel.BookViewModel

@Composable
fun DetailScreen(
    id: Int,
    vm: BookViewModel
) {
    val books by vm.books.collectAsState()
    val book = books.find { it.id == id }

    Column(Modifier.padding(16.dp)) {

        Text("Detalles del Libro", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(20.dp))

        if (book == null) {
            Text("Libro no encontrado", color = MaterialTheme.colorScheme.error)
        } else {
            Text("ID: ${book.id}")
            Text("Título: ${book.title}")
            Text("Autor: ${book.author}")
        }
    }
}
