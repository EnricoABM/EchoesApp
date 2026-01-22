package com.nohana.projetoiot.controller.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
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
        Log.d("BL", "${device.name} e ${device.address}")
            _scannedDevices.update { devices ->
                val newDevice = BluetoothDeviceMapper.toDeviceModel(device)
                if (newDevice in devices) devices else devices + newDevice
        }
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun startScan() {
//        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN))
//            return

        context.registerReceiver(
            bleReceiver,
            IntentFilter(android.bluetooth.BluetoothDevice.ACTION_FOUND)
        )
        Log.d("BL", "Adaptador: ${bleAdapter.toString()}")
        bleAdapter?.startDiscovery()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun stopScan() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN))
            return

        bleAdapter?.cancelDiscovery()
        context.unregisterReceiver(bleReceiver)
    }

    private fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

}