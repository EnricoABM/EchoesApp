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

    private val nfcAdapter by lazy {
        NfcAdapter.getDefaultAdapter(context)
    }

    private lateinit var pendingIntent: PendingIntent
    private lateinit var techList: Array<Array<String>>
    private lateinit var intentFilter: Array<IntentFilter>

    fun read(intent: Intent) {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        val ndef = Ndef.get(tag)
        val ndefMessage = ndef.cachedNdefMessage
        val records = ndefMessage.records
        if (records.isNotEmpty()) {
            val tagNfc = TagNfc(
                animal = records[0].payload.toString(),
                position = records[1].payload.toString()
            )

            Log.d("TAG", tagNfc.toString())
        }
    }

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

    fun configNfc() {
        if (nfcAdapter == null) {
            return
        }

        if (!nfcAdapter!!.isEnabled) {
            return
        }

        // Configuração da Intenção vinda do System Dyspacher
        val intent = Intent(context, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT);

        // Filtro de Intenções
        val ndefFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val techFilter = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)

        ndefFilter.addDataType("text/plain")
        techFilter.addDataType("*/*")

        intentFilter = arrayOf(ndefFilter)

        // Filtro de Tecnologias
        techList = arrayOf(
            arrayOf(Ndef::class.java.name, NdefFormatable::class.java.name),
            arrayOf(NfcA::class.java.name, NdefFormatable::class.java.name),
            arrayOf(MifareUltralight::class.java.name, NdefFormatable::class.java.name),
            arrayOf(NdefFormatable::class.java.name)
        )
    }

    fun enabledNfc() {
        configNfc()
        nfcAdapter.enableForegroundDispatch(
            context as Activity,
            pendingIntent,
            intentFilter,
            techList
        )
    }

    fun disableNfc() {
        nfcAdapter.disableForegroundDispatch(
            context as Activity
        )
    }
}