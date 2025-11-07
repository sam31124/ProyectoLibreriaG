package com.libreriag.app.ui.natives

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.libreriag.app.viewmodel.NativeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NativeScreen(vm: NativeViewModel = viewModel()) {

    val photoUri by vm.photoUri.collectAsState()
    val location by vm.location.collectAsState()
    val devices by vm.devices.collectAsState()

    val cameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* nada */ }

    val gpsPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) vm.startLocationUpdates()
    }

    val btScanPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) vm.startBluetoothScan()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recursos nativos") }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // CÁMARA
            item {
                Text("Cámara", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(8.dp))

                Button(onClick = {
                    cameraPermission.launch(Manifest.permission.CAMERA)
                }) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Dar permiso cámara")
                }

                Spacer(Modifier.height(8.dp))

                CameraPreview(
                    onPhotoCaptured = { uri ->
                        vm.setPhotoUri(uri)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                Spacer(Modifier.height(8.dp))

                photoUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Foto capturada",
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    )
                }
            }

            // GPS
            item {
                Text("GPS", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(8.dp))

                Button(onClick = {
                    gpsPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Obtener ubicación")
                }

                Spacer(Modifier.height(8.dp))

                location?.let {
                    Text("Latitud: ${it.latitude}")
                    Text("Longitud: ${it.longitude}")
                }
            }

            // BLUETOOTH
            item {
                Text("Bluetooth", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(8.dp))

                Button(onClick = {
                    btScanPermission.launch(Manifest.permission.BLUETOOTH_SCAN)
                }) {
                    Icon(Icons.Filled.Bluetooth, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Buscar dispositivos")
                }
            }

            items(devices) { device ->
                Text("• ${device.name ?: "Sin nombre"} — ${device.address}")
            }
        }
    }
}
