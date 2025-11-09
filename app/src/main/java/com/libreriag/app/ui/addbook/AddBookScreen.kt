package com.libreriag.app.ui.addbook

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.libreriag.app.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    vm: BookViewModel,
    onSaved: () -> Unit
) {
    val ctx = LocalContext.current
    val photoUri by vm.photoUri.collectAsState()

    // --- PERMISO DE CÁMARA ---
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                Toast.makeText(ctx, "Debes aceptar el permiso de cámara", Toast.LENGTH_SHORT).show()
            }
        }

    // --- LANZAR CÁMARA ---
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                Toast.makeText(ctx, "Foto tomada", Toast.LENGTH_SHORT).show()
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Agregar Libro") })
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- PREVIEW FOTO ---
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (photoUri == null) {
                    Text("Sin imagen", color = Color.DarkGray)
                } else {
                    AsyncImage(
                        model = photoUri,
                        contentDescription = "Foto libro",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // --- BOTÓN CÁMARA ---
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // pedir permiso
                    permissionLauncher.launch(Manifest.permission.CAMERA)

                    val uri = vm.createImageUri()
                    vm.setPhoto(uri)
                    cameraLauncher.launch(uri)
                }
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Tomar Foto")
            }

            // --- TÍTULO ---
            OutlinedTextField(
                value = vm.title.collectAsState().value,
                onValueChange = vm::onTitleChange,
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- AUTOR ---
            OutlinedTextField(
                value = vm.author.collectAsState().value,
                onValueChange = vm::onAuthorChange,
                label = { Text("Autor") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- GUARDAR ---
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    vm.save()
                    Toast.makeText(ctx, "Libro guardado", Toast.LENGTH_SHORT).show()
                    onSaved()
                }
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Guardar Libro")
            }
        }
    }
}
