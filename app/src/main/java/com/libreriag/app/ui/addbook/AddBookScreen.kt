package com.libreriag.app.ui.addbook

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.libreriag.app.viewmodel.BookViewModel

@Composable
fun AddBookScreen(
    vm: BookViewModel,
    onSaved: () -> Unit
) {
    val title by vm.title.collectAsState()
    val author by vm.author.collectAsState()

    val titleError by vm.titleError.collectAsState()
    val authorError by vm.authorError.collectAsState()

    Column(
        Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text("Agregar Libro", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = title,
            onValueChange = vm::onTitleChange,
            label = { Text("Título") },
            isError = titleError != null
        )
        titleError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        OutlinedTextField(
            value = author,
            onValueChange = vm::onAuthorChange,
            label = { Text("Autor") },
            isError = authorError != null
        )
        authorError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Button(
            onClick = {
                vm.save()
                if (titleError == null && authorError == null) onSaved()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}
