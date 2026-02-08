package com.nohana.projetoiot.view.nfc

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.PendingIntentCompat
import com.nohana.projetoiot.model.nfc.TagNfc
import com.nohana.projetoiot.view.components.AnimalConfigScreen
import com.nohana.projetoiot.view.ui.theme.ProjetoIotTheme
import com.nohana.projetoiot.viewmodel.AnimalViewModel

class MainNfcActivity : ComponentActivity() {
    private val nfcAdapter by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    private lateinit var intentFilter: Array<IntentFilter>
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animalViewModel = AnimalViewModel(this)

        setContent {
            ProjetoIotTheme {
                AnimalConfigScreen(
                    viewModel = animalViewModel,
                    onConfirm = { it ->
                        animalViewModel.saveToDatabase()
                    }
                )
            }
        }
    }

    private fun configNfc() {
        if (nfcAdapter == null) {
            return
        }

        if (!nfcAdapter!!.isEnabled) {
            return
        }

        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT);

        val ndefFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        ndefFilter.addDataType("text/plain")

        intentFilter = arrayOf(ndefFilter)

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            val ndefMessage = Ndef.get(tag).cachedNdefMessage
            val records = ndefMessage.records
            if (records.isNotEmpty()) {
                val tagNfc = TagNfc(
                    animal = records[0].payload.toString(),
                    position = records[1].payload.toString()
                )

                Log.d("TAG", tagNfc.toString())
            }
        }
    }
}
