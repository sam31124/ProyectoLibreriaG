package com.libreriag.app.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.libreriag.app.navigation.AppRoutes
import com.libreriag.app.viewmodel.BookViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    vm: BookViewModel
) {
    val books by vm.books.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(AppRoutes.AddBook.route)
            }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text("LibreríaG — Home", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))


            Button(
                onClick = { navController.navigate(AppRoutes.Native.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("test recursos nativos")
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(books) { b ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(AppRoutes.Detail.createRoute(b.id))
                            }
                    ) {
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
