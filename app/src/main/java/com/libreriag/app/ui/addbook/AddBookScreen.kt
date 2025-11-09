package com.libreriag.app.ui.addbook

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.libreriag.app.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    vm: BookViewModel,
    onSaved: () -> Unit
) {

    val title by vm.title.collectAsState()
    val author by vm.author.collectAsState()

    val titleError by vm.titleError.collectAsState()
    val authorError by vm.authorError.collectAsState()

    val photoUri by vm.photoUri.collectAsState()

    // --- Permisos de cámara ---
    val cameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    // --- Lanzador de cámara ---
    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (!success) return@rememberLauncherForActivityResult
        }

    // --- UI ---
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Agregar Libro") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // FOTO
            Text("Foto del libro", style = MaterialTheme.typography.titleMedium)

            if (photoUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(photoUri),
                    contentDescription = "Foto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            Button(
                onClick = {
                    cameraPermission.launch(Manifest.permission.CAMERA)

                    val uri: Uri = vm.createImageUri()
                    vm.setPhoto(uri)
                    takePictureLauncher.launch(uri)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Tomar Foto")
            }

            // TÍTULO
            OutlinedTextField(
                value = title,
                onValueChange = vm::onTitleChange,
                label = { Text("Título") },
                isError = titleError != null,
                supportingText = {
                    if (titleError != null) Text(titleError!!, color = Color.Red)
                },
                modifier = Modifier.fillMaxWidth()
            )

            // AUTOR
            OutlinedTextField(
                value = author,
                onValueChange = vm::onAuthorChange,
                label = { Text("Autor") },
                isError = authorError != null,
                supportingText = {
                    if (authorError != null) Text(authorError!!, color = Color.Red)
                },
                modifier = Modifier.fillMaxWidth()
            )

            // BOTÓN GUARDAR
            Button(
                onClick = {
                    vm.save()
                    onSaved()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Guardar libro")
            }
        }
    }
}
