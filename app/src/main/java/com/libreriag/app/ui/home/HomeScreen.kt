package com.libreriag.app.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.libreriag.app.viewmodel.BookViewModel
import androidx.compose.ui.unit.dp
import com.libreriag.app.navigation.AppRoutes
import androidx.compose.material.icons.filled.Person




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, vm: BookViewModel) {

    val books by vm.books.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Librería-G") },
                actions = {
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
            FloatingActionButton(onClick = { navController.navigate("add_book") }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar libro")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            books.forEach { book ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("detail/${book.id}")
                        }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(book.title)
                        Text("Autor: ${book.author}")
                    }
                }
            }
        }
    }
}
