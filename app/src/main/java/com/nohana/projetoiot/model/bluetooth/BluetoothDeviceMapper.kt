package com.nohana.projetoiot.model.bluetooth

import android.bluetooth.BluetoothDevice

class BluetoothDeviceMapper {
    companion object {
        fun toDeviceModel(device: BluetoothDevice): Device {
            return Device(
                name = device.name,
                address = device.address
            )
        }
    }
}