package com.libreriag.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.libreriag.app.ui.addbook.AddBookScreen
import com.libreriag.app.ui.home.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppRoutes.Home.route) {
        composable(AppRoutes.Home.route) { HomeScreen(navController) }
        composable(AppRoutes.AddBook.route) { AddBookScreen(onSaved = { navController.popBackStack() }) }
    }
}
