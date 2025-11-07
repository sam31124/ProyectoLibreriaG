package com.libreriag.app.ui.natives

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.libreriag.app.viewmodel.NativeViewModel

@Composable
fun NativeScreen(vm: NativeViewModel = viewModel()) {

    val context = LocalContext.current
    val photoUri by vm.photoUri.collectAsState()
    val location by vm.location.collectAsState()
    val devices by vm.devices.collectAsState()

    // PERMISOS
    val cameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            // nada extra
        }
    }

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // CÁMARA
        item {
            Text("Cámara", style = MaterialTheme.typography.titleLarge)

            Button(onClick = {
                cameraPermission.launch(Manifest.permission.CAMERA)
            }) {
                Text("Dar permiso cámara")
            }

            CameraPreview(
                onPhotoCaptured = { uri ->
                    vm.setPhotoUri(uri)
                },
                modifier = Modifier.fillMaxWidth()
            )

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
            Text("GPS", style = MaterialTheme.typography.titleLarge)

            Button(onClick = {
                gpsPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }) {
                Text("Obtener Ubicación")
            }

            location?.let {
                Text("Latitud: ${it.latitude}")
                Text("Longitud: ${it.longitude}")
            }
        }

        // BLUETOOTH
        item {
            Text("Bluetooth", style = MaterialTheme.typography.titleLarge)

            Button(onClick = {
                btScanPermission.launch(Manifest.permission.BLUETOOTH_SCAN)
            }) {
                Text("Buscar dispositivos")
            }
        }

        items(devices) { device ->
            Text("• ${device.name ?: "Sin nombre"} — ${device.address}")
        }
    }
}
