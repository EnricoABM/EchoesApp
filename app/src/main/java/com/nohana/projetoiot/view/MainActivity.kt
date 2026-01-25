package com.nohana.projetoiot.view

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.CaseMap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nohana.projetoiot.view.components.TitleHeader
import com.nohana.projetoiot.view.ui.theme.ProjetoIotTheme
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

        enableEdgeToEdge()
        setContent {
            ProjetoIotTheme {
                checkAndRequestPermission()

                val viewModel = BluetoothViewModel(this)
                val state by viewModel.state.collectAsState()

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
                                    items = state.devices,
                                ) { device ->
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { viewModel.connectToDevice(device) },
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
                        Button(onClick = viewModel::startScan) {
                            Text(
                                fontSize = 16.sp,
                                text = "Iniciar Varredura"
                            )
                        }
                        Button(onClick = viewModel::stopScan) {
                            Text(
                                fontSize = 16.sp,
                                text = "Parar Varredura"
                            )
                        }
                    }

                }
            }
        }
    }
}
