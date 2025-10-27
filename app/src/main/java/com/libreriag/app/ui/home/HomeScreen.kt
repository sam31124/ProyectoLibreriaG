package com.libreriag.app.ui.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.libreriag.app.navigation.AppRoutes

@Composable
fun HomeScreen(navController: NavController) {
    Button(onClick = { navController.navigate(AppRoutes.ADD) }) {
        Text("Ir a agregar libro")
    }
}
