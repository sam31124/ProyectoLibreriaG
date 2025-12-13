package com.libreriag.app.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.libreriag.app.R
import com.libreriag.app.viewmodel.BookViewModel

@Composable
fun ProfileScreen(navController: NavController, viewModel: BookViewModel) {

    // Observamos al usuario actual desde el ViewModel
    val user by viewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // LOGO (Tu recurso)
        // Asegúrate de que la imagen exista en res/drawable/libreria_g_logo
        Image(
            painter = painterResource(R.drawable.libreria_g_logo),
            contentDescription = "Logo LibreriaG",
            modifier = Modifier
                .size(140.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            "Mi Perfil",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjeta de Información
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                if (user != null) {
                    Text("Usuario: ${user?.name}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Correo: ${user?.email}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Rol: ${user?.role?.uppercase()}", color = MaterialTheme.colorScheme.primary)
                } else {
                    Text("No hay sesión activa.", color = Color.Red)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // BOTÓN CERRAR SESIÓN (Obligatorio para el flujo de usuario)
        Button(
            onClick = {
                viewModel.logout()
                // Navegar al Login y limpiar el historial para que no pueda volver atrás
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("CERRAR SESIÓN")
        }
    }
}