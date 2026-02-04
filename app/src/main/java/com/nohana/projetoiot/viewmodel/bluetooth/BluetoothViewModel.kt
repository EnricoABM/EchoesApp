package com.nohana.projetoiot.viewmodel.bluetooth

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nohana.projetoiot.controller.bluetooth.BluetoothController
import com.nohana.projetoiot.controller.bluetooth.ConnectionResult
import com.nohana.projetoiot.model.Device
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BluetoothViewModel(
    context: Context
): ViewModel() {

    private val controller: BluetoothController = BluetoothController(context)
    private val _state = MutableStateFlow(BluetoothUiState())
    val state = combine(
        controller.scannedDevices,
        _state
    ) { scannedDevices, state ->
        state.copy(
            devices = scannedDevices
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    private var deviceConnectionJob: Job?  = null

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun startScan() {
        Log.d("BL", "Iniciando Varredura")
        controller.startScan()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun stopScan() {
        Log.d("BL", "Parando Varredura")
        controller.stopScan()
    }

    fun connectToDevice(device: Device): Unit {
        _state.update { it.copy(
            isConnecting = true
        ) }
        deviceConnectionJob = controller.connectToDevice(device).listen()
    }

    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        controller.closeConnection()
        _state.update { it.copy(
            isConnecting = false,
            isConnected = false
        )}
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            controller.sendMessage(message);
        }
    }

    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when(result) {
                ConnectionResult.ConnectionEstablished -> {
                    _state.update { it.copy(
                        isConnected = true,
                        isConnecting = false,
                        errorMessage = null
                    ) }
                }
                is ConnectionResult.TransferSucceeded -> {

                }
                is ConnectionResult.Error -> {

                }
            }
        }
            .catch { throwable ->
                controller.closeConnection()
                _state.update { it.copy(
                    isConnected = false,
                    isConnecting = false,
                ) }
            }
            .launchIn(viewModelScope)
    }

}