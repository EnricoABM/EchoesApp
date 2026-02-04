package com.nohana.projetoiot.viewmodel.bluetooth

import com.nohana.projetoiot.model.Device

data class BluetoothUiState(
    val devices: List<Device> = emptyList(),
    val error: String? = null,
    val isConnecting: Boolean = false,
    val isConnected: Boolean = false,
    val errorMessage: String? = null
) {
}