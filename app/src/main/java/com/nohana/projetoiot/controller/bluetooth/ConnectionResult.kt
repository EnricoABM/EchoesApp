package com.nohana.projetoiot.controller.bluetooth

sealed interface ConnectionResult {
    object ConnectionEstablished: ConnectionResult
    data class TransferSucceeded(val data: String): ConnectionResult
    data class Error(val message: String): ConnectionResult
}