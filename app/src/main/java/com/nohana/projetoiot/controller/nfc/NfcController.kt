package com.nohana.projetoiot.controller.nfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareUltralight
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.nfc.tech.NfcA
import android.util.Log
import com.nohana.projetoiot.model.nfc.TagNfc

class NfcController(
    private val context: Context
) {


    fun write(intent: Intent) {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        val ndef = Ndef.get(tag)
        if (ndef.isWritable) {
            val nfcMessage = NdefMessage(
                arrayOf(
                    NdefRecord.createTextRecord("en", "Animal"),
                    NdefRecord.createTextRecord("en", "Posicao")
                )
            )

            ndef.connect()
            ndef.writeNdefMessage(nfcMessage)
            ndef.close()
        }
    }
}