//package com.example.projetoic.service
//
//import android.app.Activity
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.nfc.NfcAdapter
//import android.nfc.Tag
//import android.nfc.tech.Ndef
//import android.nfc.tech.NfcF
//import android.os.Build
//import android.view.View
//import android.widget.Toast
//import com.nohana.projetoiot.model.nfc.TagNfc
//
//class NfcService {
//    private val nfcAdapter : NfcAdapter?
//    private val activity: Activity
//    private var intentFilterArray : Array<IntentFilter>? = null
//    private var pi: PendingIntent? = null
//    private val techListsArray = arrayOf(arrayOf(NfcF::class.java.name))
//
//    constructor(nfcAdapter: NfcAdapter?, activity: Activity) {
//        this.nfcAdapter = nfcAdapter
//        this.activity = activity
//    }
//
//    fun read(intent: Intent): TagNfc? {
//        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return null
//        val ndef = Ndef.get(tag) ?: return null
//
//
//        try {
//            ndef.connect()
//            val ndefMessage = ndef.cachedNdefMessage
//            val records = ndefMessage.records
//            var tagNfc: TagNfc? = null
//            if (records.isNotEmpty()) {
//                val animal = String(records[0].payload).drop(3)
//                val position = String(records[1].payload).drop(3)
//                tagNfc = TagNfc(animal, position)
//            }
//
//            ndef.close()
//            return tagNfc
//        } catch (e: Exception) {
//            return null
//        }
//    }
//
//    fun write() {
//
//    }
//
//    fun configNfc(): String {
//        if (nfcAdapter == null) {
//            return "NFC não disponivel"
//        } else if (!nfcAdapter!!.isEnabled) {
//            return "NFC desligado"
//        }
//        val intent = Intent(activity, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            // PendingIntent NFC acima da versão 13 precisam receber a Flag MUTABLE
//            pi = PendingIntent.getActivity(activity.baseContext, 0, intent, android.app.PendingIntent.FLAG_MUTABLE)
//        } else {
//            pi = PendingIntent.getActivity(activity.baseContext, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT);
//        }
//
//        val ndefFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
//        val tagFilter = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
//        val techFilter = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
//
//        try {
//            ndefFilter.addDataType("*/*")
//            tagFilter.addDataType("*/*")
//            techFilter.addDataType("*/*")
//        } catch (e : IntentFilter.MalformedMimeTypeException) {
//            throw RuntimeException("Erro")
//        }
//
//        intentFilterArray = arrayOf<IntentFilter> (
//            ndefFilter,
//            tagFilter,
//            techFilter
//        )
//        nfcAdapter?.enableForegroundDispatch(activity, pi, intentFilterArray, techListsArray)
//        return "Aproxime da Tag"
//    }
//
//    fun enableNfc() {
//        nfcAdapter?.enableForegroundDispatch(activity, pi, intentFilterArray, techListsArray)
//    }
//
//    fun disableNfc() {
//        nfcAdapter?.disableForegroundDispatch(activity)
//    }
//}