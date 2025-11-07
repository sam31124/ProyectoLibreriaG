package com.libreriag.app.ui.addbook

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.libreriag.app.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    vm: BookViewModel,
    onSaved: () -> Unit
) {
    val title by vm.title.collectAsState()
    val author by vm.author.collectAsState()

    val titleError by vm.titleError.collectAsState()
    val authorError by vm.authorError.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar libro") }
            )
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = vm::onTitleChange,
                label = { Text("Título") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null
                    )
                },
                isError = titleError != null,
                modifier = Modifier.fillMaxWidth()
            )
            titleError?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = author,
                onValueChange = vm::onAuthorChange,
                label = { Text("Autor") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null
                    )
                },
                isError = authorError != null,
                modifier = Modifier.fillMaxWidth()
            )
            authorError?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    vm.save()
                    if (titleError == null && authorError == null) onSaved()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text("Guardar")
            }
        }
    }
}
