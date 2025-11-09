package com.libreriag.app.navigation

sealed class AppRoutes(val route: String) {

    data object Home : AppRoutes("home")

    data object AddBook : AppRoutes("add_book")

    data object Detail : AppRoutes("detail/{id}") {
        fun createRoute(id: Int) = "detail/$id"
    }

}

