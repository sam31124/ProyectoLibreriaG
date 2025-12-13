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
    val user by vm.currentUser.collectAsState() // Obtenemos el usuario actual
    val context = LocalContext.current

    // --- LÓGICA DE ROLES (PERMISOS) ---
    // 1. Admin: Todo
    // 2. Editor: Todo menos borrar
    // 3. User: Solo ver
    // 4. Guest: Solo ver (sin perfil)

    val role = user?.role ?: "guest"
    val canAdd = role == "admin" || role == "editor"
    val canEdit = role == "admin" || role == "editor"
    val canDelete = role == "admin"
    val hasProfile = role != "guest"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Librería-G")
                        // Mostramos el rol para que sepas con quién estás probando
                        Text(
                            text = "Rol: ${role.uppercase()}",
                            fontSize = 12.sp,
                            lineHeight = 14.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                actions = {
                    // Botón Sincronizar (Para todos)
                    IconButton(onClick = {
                        Toast.makeText(context, "🔄 Sincronizando...", Toast.LENGTH_SHORT).show()
                        vm.sincronizarConNube()
                        vm.cargarRecomendaciones()
                    }) {
                        Icon(Icons.Default.Refresh, "Sincronizar")
                    }

                    // Botón Perfil (Solo si no es invitado)
                    if (hasProfile) {
                        IconButton(onClick = { navController.navigate("profile") }) {
                            Icon(Icons.Default.Person, "Perfil")
                        }
                    } else {
                        // Botón Salir Rápido (Para invitados)
                        TextButton(onClick = {
                            vm.logout()
                            navController.navigate("login") { popUpTo("home") { inclusive = true } }
                        }) {
                            Text("SALIR")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            // Solo mostramos el botón + si tiene permiso (Admin o Editor)
            if (canAdd) {
                FloatingActionButton(onClick = {
                    vm.clearForm()
                    navController.navigate("add_book")
                }) {
                    Icon(Icons.Default.Add, "Agregar")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección API Externa
            item {
                Text("🌐 Novedades IT (API Externa)", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                if (recommendedBooks.isNotEmpty()) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(recommendedBooks) { book -> ITBookItem(book) }
                    }
                } else {
                    Text("Cargando novedades...", fontSize = 12.sp, color = Color.Gray)
                }
            }

            item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }

            // Sección Tus Libros
            item { Text("📚 Mis Libros (AWS)", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }

            if (books.isEmpty()) item { Text("No hay libros guardados.") }

            items(books) { book ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable {
                        // Lógica de click en el libro (Editar o Ver Detalle)
                        if (canEdit) {
                            vm.prepareUpdate(book)
                            navController.navigate("add_book")
                        } else {
                            Toast.makeText(context, "Modo lectura: No tienes permisos para editar", Toast.LENGTH_SHORT).show()
                            // Aquí podrías navegar al detalle si quisieras: navController.navigate("detail/${book.id}")
                        }
                    },
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(book.title, fontWeight = FontWeight.Bold)
                            Text("Autor: ${book.author}", fontSize = 14.sp)
                        }

                        // Solo mostramos borrar si es ADMIN
                        if (canDelete) {
                            IconButton(onClick = { vm.deleteBook(book) }) {
                                Icon(Icons.Default.Delete, "Borrar", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ITBookItem(book: ITBook) {
    Card(modifier = Modifier.width(120.dp).height(180.dp), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(model = book.imageUrl, contentDescription = null, modifier = Modifier.fillMaxWidth().height(90.dp))
            Text(book.title, maxLines = 2, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}