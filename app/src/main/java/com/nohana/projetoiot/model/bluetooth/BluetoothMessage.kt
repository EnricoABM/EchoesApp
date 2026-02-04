package com.nohana.projetoiot.model.bluetooth

class BluetoothMessage(
    private val message: String
) {
    fun toByteArray(): ByteArray {
        return message.toByteArray()
    }
}