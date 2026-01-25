package com.nohana.projetoiot.controller.bluetooth

class BluetoothMessage(
    private val message: String
) {
    fun toByteArray(): ByteArray {
        return message.toByteArray()
    }
}
