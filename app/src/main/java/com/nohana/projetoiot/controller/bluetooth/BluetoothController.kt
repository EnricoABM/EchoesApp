package com.nohana.projetoiot.controller.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.RequiresPermission
import com.nohana.projetoiot.model.bluetooth.BluetoothDeviceMapper
import com.nohana.projetoiot.model.bluetooth.BluetoothMessage
import com.nohana.projetoiot.model.bluetooth.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import java.io.IOException
import java.util.UUID

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

    @SuppressLint("MissingPermission")
    private val bleReceiver = BluetoothReceiver { device ->
        Log.d("BL", "${device.name} e ${device.address}")
            _scannedDevices.update { devices ->
                val newDevice = BluetoothDeviceMapper.toDeviceModel(device)
                Log.d("BL", devices.toString())
                if (newDevice in devices) devices else devices + newDevice
            }
    }
    private var bleSocket: BluetoothSocket? = null

    private var bleDataService: BluetoothDataTrasferService? = null

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
//        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN))
//            return

        bleAdapter?.cancelDiscovery()
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: Device): Flow<ConnectionResult> {
        return flow {
//           if(!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
//                throw SecurityException("Permissão Faltante: BLUETOOTH_CONNECT")
//            }

            bleSocket = bleAdapter
                ?.getRemoteDevice(device.address)
                ?.createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_UUID))

            Log.d("BL" , "Conectando se ao dispostivo: ${device.name}")
            stopScan()

            bleSocket?.let { socket ->
                try {
                    socket.connect()
                    Log.d("BL", "Dispositivo Conectado")
                    bleDataService = BluetoothDataTrasferService(socket)
                    emit(ConnectionResult.ConnectionEstablished)
                } catch (e : IOException) {
                    socket.close()
                    bleSocket = null
                    emit(ConnectionResult.Error("Conexão Interrompida"))
                }
            }
        }.onCompletion {
            // closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendMessage(message: String) {
//        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT))
//            return
        if (bleDataService == null)
            return

        val bleMessage = BluetoothMessage(message)

        bleDataService?.sendMessage(bleMessage)
    }

    fun closeConnection() {
        bleSocket?.close()
        bleSocket = null
    }

    fun release() {
        context.unregisterReceiver(bleReceiver)
        closeConnection()
    }

    private fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        val SERVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    }
}