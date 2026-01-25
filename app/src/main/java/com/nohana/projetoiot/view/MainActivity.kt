package com.nohana.projetoiot.view

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.nohana.projetoiot.model.Device
import com.nohana.projetoiot.view.components.TitleHeader
import com.nohana.projetoiot.view.ui.theme.ProjetoIotTheme
import com.nohana.projetoiot.viewmodel.BluetoothUiState
import com.nohana.projetoiot.viewmodel.BluetoothViewModel

class MainActivity : ComponentActivity() {

    private val bluetoothManager: BluetoothManager? by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        bluetoothManager?.adapter
    }

    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    private val requestMultiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val denied = permissions.filter { !it.value }.keys
        if (denied.isNotEmpty()) {
            Toast.makeText(this, "Permissões necessárias não concedidas", Toast.LENGTH_SHORT).show()
        }
    }

    private val enabledBtLaucher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { /* Não necessário, Bluetooth verificado no código */ }

    private fun checkBluetoothEnabled() {
        if (!isBluetoothEnabled) {
            enabledBtLaucher.launch(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            )
        }
    }

    private fun checkAndRequestPermission() {

            val missingPermissions = mutableListOf<String>()

            // Verificar e adicionar permissões faltando
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(Manifest.permission.BLUETOOTH_SCAN)
            }

            if (missingPermissions.isNotEmpty()) {
                requestMultiplePermissionsLauncher.launch(missingPermissions.toTypedArray())
            }

            Log.d("BL", "Permissões: ${missingPermissions.toString()}")

            checkBluetoothEnabled()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = BluetoothViewModel(this)

        enableEdgeToEdge()
        setContent {
            ProjetoIotTheme {
                checkAndRequestPermission()

                val state by viewModel.state.collectAsState()

                DevicesScreen(
                    state,
                    viewModel::startScan,
                    viewModel::stopScan,
                    viewModel::connectToDevice,
                )
            }
        }
    }
}

@Composable
fun DevicesScreen(
    state: BluetoothUiState,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    onClickDevice: (Device) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(
                top = 60.dp,
                start = 10.dp,
                end = 10.dp,
                bottom = 60.dp
            ).fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TitleHeader("Dispositivos")

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            when {
                !state.devices.isEmpty() -> {
                    items(
                        items = state.devices
                    ) { device ->
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onClickDevice(device) },
                            text = "${device.name}",
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    item{
                        Text(
                            modifier = Modifier.fillMaxSize(),
                            text = "Nenhum Dispositivo Encontrado",
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = onStartScan) {
                Text(
                    fontSize = 16.sp,
                    text = "Iniciar Varredura"
                )
            }
            Button(onClick = onStopScan) {
                Text(
                    fontSize = 16.sp,
                    text = "Parar Varredura"
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {Log.d("BL VIEW", state.devices.toString())}
            ) {
                Text("Mostrar Lista")
            }
        }
    }
}