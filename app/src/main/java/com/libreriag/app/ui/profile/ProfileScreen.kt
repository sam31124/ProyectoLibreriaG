package com.libreriag.app.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.libreriag.app.R

@Composable
fun ProfileScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // LOGO
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

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Usuario: Samuel", fontSize = 18.sp)
                Text("Correo: sa.urzua@duocuc.cl")
                Text("Aplicación: LibreriaG")
            }
        }

    }
}
