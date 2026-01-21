package com.nohana.projetoiot.viewmodel

import com.nohana.projetoiot.model.Device

data class BluetoothUiState(
    val devices: List<Device> = emptyList(),
    val error: String? = null
) {
}