package com.libreriag.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.libreriag.app.ui.addbook.AddBookScreen
import com.libreriag.app.ui.detail.DetailScreen
import com.libreriag.app.ui.home.HomeScreen
import com.libreriag.app.ui.profile.ProfileScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.HOME
    ) {
        composable(AppRoutes.HOME) { HomeScreen(navController) }
        composable(AppRoutes.ADD) { AddBookScreen() }
        composable(AppRoutes.DETAIL) { DetailScreen() }
        composable(AppRoutes.PROFILE) { ProfileScreen() }
    }
}
