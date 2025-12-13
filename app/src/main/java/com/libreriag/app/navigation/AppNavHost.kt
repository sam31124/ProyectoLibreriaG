package com.libreriag.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.libreriag.app.ui.addbook.AddBookScreen
import com.libreriag.app.ui.detail.DetailScreen
import com.libreriag.app.ui.home.HomeScreen
import com.libreriag.app.ui.login.LoginScreen
import com.libreriag.app.ui.login.RegisterScreen
import com.libreriag.app.ui.profile.ProfileScreen
import com.libreriag.app.viewmodel.BookViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    bookViewModel: BookViewModel
) {
    // CAMBIO: startDestination ahora es "login" para forzar autenticación
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // 1. Pantalla de Login
        composable("login") {
            LoginScreen(navController, bookViewModel)
        }

        // 2. Pantalla de Registro
        composable("register") {
            RegisterScreen(navController, bookViewModel)
        }

        // 3. Home (Lista de libros)
        composable("home") {
            HomeScreen(navController, bookViewModel)
        }

        // 4. Agregar Libro (Adapta el nombre de ruta a "add_book" que usamos en HomeScreen)
        composable("add_book") {
            // Mantenemos tu estructura: pasamos el VM y la acción de volver
            AddBookScreen(vm = bookViewModel) {
                navController.popBackStack()
            }
        }

        // 5. Perfil (Ahora recibe parámetros)
        composable("profile") {
            ProfileScreen(navController, bookViewModel)
        }

        // 6. Detalle (Adapta la ruta a "detail/{id}")
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: -1
            DetailScreen(id = id, vm = bookViewModel)
        }
    }
}