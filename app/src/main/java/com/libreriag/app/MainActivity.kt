package com.libreriag.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.libreriag.app.navigation.AppNavHost
import com.libreriag.app.viewmodel.BookViewModel

class MainActivity : ComponentActivity() {

    private val bookViewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            AppNavHost(
                navController = navController,
                bookViewModel = bookViewModel
            )
        }
    }
}
