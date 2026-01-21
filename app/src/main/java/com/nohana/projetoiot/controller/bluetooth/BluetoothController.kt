package com.nohana.projetoiot.controller.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import com.nohana.projetoiot.model.Device
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BluetoothController (
    private val context: Context
) {

    private val _scannedDevices = MutableStateFlow<List<Device>>(emptyList())
    val scannedDevices = _scannedDevices.asStateFlow()

    private val bleManager: BluetoothManager? by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bleAdapter: BluetoothAdapter? by lazy {
        bleManager?.adapter
    }

    private val bleReceiver = BluetoothReceiver { device ->
            _scannedDevices.update { devices ->
                val newDevice = BluetoothDeviceMapper.toDeviceModel(device)
                if (newDevice in devices) devices else devices + newDevice
        }
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun startScan() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN))
            return

        context.registerReceiver(
            bleReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )

        bleAdapter?.startDiscovery()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun stopScan() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN))
            return

        bleAdapter?.cancelDiscovery()
    }

    private fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

}