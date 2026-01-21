package com.nohana.projetoiot.controller.bluetooth

import android.bluetooth.BluetoothDevice
import com.nohana.projetoiot.model.Device

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