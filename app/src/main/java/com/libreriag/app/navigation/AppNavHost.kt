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
import com.libreriag.app.ui.natives.NativeScreen
import com.libreriag.app.viewmodel.BookViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    bookViewModel: BookViewModel
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.Home.route
    ) {
        composable(AppRoutes.Home.route) {
            HomeScreen(navController, bookViewModel)
        }

        composable(AppRoutes.AddBook.route) {
            AddBookScreen(vm = bookViewModel) {
                navController.popBackStack()
            }
        }

        composable(AppRoutes.Native.route) {
            NativeScreen()
        }

        composable(
            route = AppRoutes.Detail.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: -1
            DetailScreen(id = id, vm = bookViewModel)
        }
    }
}
