package com.libreriag.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.libreriag.app.data.local.BookDatabase
import com.libreriag.app.data.repository.BookRepository
import com.libreriag.app.ui.home.HomeScreen
import com.libreriag.app.viewmodel.BookViewModel
import com.libreriag.app.viewmodel.BookViewModelFactory

class MainActivity : ComponentActivity() {

    // Inyección del ViewModel usando Factory + Room + Repository
    private val viewModel: BookViewModel by viewModels {
        val dao = BookDatabase.getDatabase(this).bookDao()
        val repository = BookRepository(dao)
        BookViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface {
                    // ViewModel a la pantalla
                    HomeScreen(viewModel)
                }
            }
        }
    }
}
