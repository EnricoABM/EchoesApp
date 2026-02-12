package com.nohana.projetoiot.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.nohana.projetoiot.controller.nfc.NfcController

class NfcViewModel(
    private val context: Context
): ViewModel() {

    private val controller = NfcController(context)

    init {
        controller.configNfc()
    }

    fun read(intent: Intent) {
        controller.read(intent)
    }

    fun write(intent: Intent) {
        controller.write(intent)
    }

    fun enabledNfc() {
        controller.enabledNfc()
    }

    fun disableNfc() {
        controller.disableNfc()
    }

}