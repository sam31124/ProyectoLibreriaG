package com.libreriag.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.libreriag.app.navigation.AppRoutes
import com.libreriag.app.viewmodel.BookViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val vm: BookViewModel = viewModel()
    val books by vm.books.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(AppRoutes.AddBook.route) }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("LibreríaG — Home", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(books) { b ->
                    ElevatedCard {
                        Column(Modifier.padding(12.dp)) {
                            Text(b.title, style = MaterialTheme.typography.titleMedium)
                            Text("Autor: ${b.author}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
