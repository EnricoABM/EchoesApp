package com.nohana.projetoiot.viewmodel

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.nohana.projetoiot.controller.bluetooth.BluetoothController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class BluetoothViewModel(
        private val controller: BluetoothController,
        private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(BluetoothUiState())
    val state = combine(
        controller.scannedDevices,
        _state
    ) { scannedDevices, state ->
        state.copy(
            devices = scannedDevices
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun startScan() {
        controller.startScan()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun stopScan() {
        controller.stopScan()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras): T {
                    val application = checkNotNull(extras[APPLICATION_KEY])
                    val savedStateHandler = extras.createSavedStateHandle()
                return BluetoothViewModel(
                    BluetoothController(application),
                    savedStateHandler
                ) as T
            }
        }
    }
}