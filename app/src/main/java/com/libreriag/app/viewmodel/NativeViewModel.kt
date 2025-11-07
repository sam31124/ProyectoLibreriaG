package com.libreriag.app.viewmodel

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NativeViewModel(application: Application) : AndroidViewModel(application) {

    // FOTO
    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> = _photoUri
    fun setPhotoUri(uri: Uri) { _photoUri.value = uri }

    // GPS
    private val fusedClient = LocationServices.getFusedLocationProviderClient(application)
    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    @android.annotation.SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val context = getApplication<Application>()
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permiso GPS no concedido", Toast.LENGTH_SHORT).show()
            return
        }

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000
        ).build()

        fusedClient.requestLocationUpdates(request, object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                _location.value = result.lastLocation
            }
        }, Looper.getMainLooper())
    }

    // BLUETOOTH
    private val adapter = BluetoothAdapter.getDefaultAdapter()
    private val _devices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val devices: StateFlow<List<BluetoothDevice>> = _devices

    private var receiver: BroadcastReceiver? = null

    fun startBluetoothScan() {
        val context = getApplication<Application>()

        if (adapter == null) {
            Toast.makeText(context, "Bluetooth no soportado", Toast.LENGTH_SHORT).show()
            return
        }

        _devices.value = emptyList()

        if (receiver == null) {
            receiver = object : BroadcastReceiver() {
                override fun onReceive(ctx: Context?, intent: Intent?) {
                    if (intent?.action == BluetoothDevice.ACTION_FOUND) {
                        val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        device?.let { _devices.value = _devices.value + it }
                    }
                }
            }
            context.registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        }

        adapter.startDiscovery()
    }

    override fun onCleared() {
        super.onCleared()
        val context = getApplication<Application>()
        receiver?.let { context.unregisterReceiver(it) }
    }
}
