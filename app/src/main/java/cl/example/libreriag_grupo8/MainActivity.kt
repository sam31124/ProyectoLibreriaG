package cl.example.libreriag_grupo8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cl.example.libreriag_grupo8.ui.HomeScreen
import cl.example.libreriag_grupo8.ui.theme.LibreriaG_Grupo8Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // esto  ayuda a aplicar el tema global de la app
            LibreriaG_Grupo8Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // llama al archivo HomeScreen
                    HomeScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    LibreriaG_Grupo8Theme {
        HomeScreen()
    }
}
