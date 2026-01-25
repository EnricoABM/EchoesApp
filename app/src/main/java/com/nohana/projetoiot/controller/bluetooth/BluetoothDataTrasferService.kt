package com.nohana.projetoiot.controller.bluetooth

import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class BluetoothDataTrasferService(
    private val socket: BluetoothSocket
) {

    suspend fun sendMessage(message: BluetoothMessage): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                socket.outputStream.write(message.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
                // Sai apenas da função withContext e retorna false
                return@withContext false
            }
            // Retorna True
            true
        }
    }
}