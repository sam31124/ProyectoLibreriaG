package com.libreriag.app.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.libreriag.app.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: Int,
    vm: BookViewModel
) {
    val books by vm.books.collectAsState()
    val book = books.find { it.id == id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Libro") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (book == null) {
                Text(
                    "Libro no encontrado",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleLarge
                )
            } else {

                //------------------------------------------
                // FOTO DEL LIBRO
                //------------------------------------------
                if (book.photo != null) {
                    Image(
                        painter = rememberAsyncImagePainter(book.photo),
                        contentDescription = "Foto del libro",
                        modifier = Modifier
                            .size(220.dp)
                            .padding(8.dp),
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Book,
                        contentDescription = null,
                        modifier = Modifier.size(150.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                //------------------------------------------
                // DETALLES
                //------------------------------------------
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text("ID: ${book.id}", style = MaterialTheme.typography.titleMedium)
                        Text("Título:", style = MaterialTheme.typography.labelLarge)
                        Text(book.title, style = MaterialTheme.typography.bodyLarge)

                        Spacer(Modifier.height(8.dp))

                        Text("Autor:", style = MaterialTheme.typography.labelLarge)
                        Text(book.author, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
