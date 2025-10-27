package com.libreriag.app.ui.addbook

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.libreriag.app.viewmodel.BookViewModel

@Composable
fun AddBookScreen(onSaved: () -> Unit) {
    val vm: BookViewModel = viewModel()
    val title by vm.title.collectAsState()
    val author by vm.author.collectAsState()
    val titleError by vm.titleError.collectAsState()
    val authorError by vm.authorError.collectAsState()

    Scaffold { padding ->
        Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Agregar Libro", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = title,
                onValueChange = vm::onTitleChange,
                label = { Text("Título") },
                isError = titleError != null,
                supportingText = { if (titleError != null) Text(titleError!!) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = author,
                onValueChange = vm::onAuthorChange,
                label = { Text("Autor") },
                isError = authorError != null,
                supportingText = { if (authorError != null) Text(authorError!!) },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { vm.save(); if (titleError == null && authorError == null) onSaved() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar") }
        }
    }
}
