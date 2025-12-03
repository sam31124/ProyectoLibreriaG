package com.libreriag.app.ui.home

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
// 👇 Importamos el icono de refrescar
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.libreriag.app.viewmodel.BookViewModel
import com.libreriag.app.data.remote.ITBook
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, vm: BookViewModel) {

    val books by vm.books.collectAsState()
    val recommendedBooks by vm.recommendedBooks.collectAsState()
    val context = LocalContext.current // Necesario para mostrar mensajes (Toasts)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Librería-G") },
                actions = {
                    // --- 🔄 BOTÓN DE SINCRONIZAR (CUMPLE REQUISITO "RECIBIR DATOS") ---
                    IconButton(onClick = {
                        // Feedback visual para que sepas que sí funcionó el clic
                        Toast.makeText(context, "🔄 Sincronizando con la nube...", Toast.LENGTH_SHORT).show()

                        // Esto hace que la app vaya a Ubuntu y traiga lo nuevo (ej: lo de Postman)
                        vm.sincronizarConNube()
                        vm.cargarRecomendaciones()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sincronizar datos"
                        )
                    }

                    // Botón Perfil
                    IconButton(onClick = {
                        navController.navigate("profile")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                vm.clearForm() // Limpiamos para que no salga "Editar" si queríamos "Crear"
                navController.navigate("add_book")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar libro")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- SECCIÓN 1: API EXTERNA ---
            item {
                Text(
                    text = "🌐 Novedades IT (API Externa)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text("Fuente: api.itbook.store", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))

                if (recommendedBooks.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(recommendedBooks) { book ->
                            ITBookItem(book)
                        }
                    }
                }
            }

            item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }

            // --- SECCIÓN 2: TUS LIBROS (AWS + Local) ---
            item {
                Text(
                    text = "📚 Mis Libros",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (books.isEmpty()) {
                item { Text("No hay libros. Dale al botón 🔄 para descargar.") }
            }

            items(books) { book ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Al tocar un libro, lo preparamos para EDITAR (Update)
                            vm.prepareUpdate(book)
                            navController.navigate("add_book")
                        },
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = book.title, fontWeight = FontWeight.Bold)
                            Text(text = "Autor: ${book.author}", fontSize = 14.sp)
                        }

                        // Botón Borrar (Delete)
                        IconButton(onClick = { vm.deleteBook(book) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

// Tarjeta para libros de la API Externa
@Composable
fun ITBookItem(book: ITBook) {
    Card(
        modifier = Modifier.width(120.dp).height(180.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = book.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(90.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = book.title, maxLines = 2, fontSize = 11.sp, lineHeight = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}