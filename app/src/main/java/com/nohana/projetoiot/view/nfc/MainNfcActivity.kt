package com.nohana.projetoiot.view.nfc

import android.app.PendingIntent
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
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import com.nohana.projetoiot.viewmodel.NfcViewModel

class MainNfcActivity : ComponentActivity() {
    private lateinit var nfcViewModel: NfcViewModel

    private var writableMode = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animalViewModel = AnimalViewModel(this)
        nfcViewModel = NfcViewModel(this)
        nfcViewModel.enabledNfc()

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

    override fun onPause() {
        super.onPause()
        nfcViewModel.disableNfc()
    }

    override fun onResume() {
        super.onResume()
        nfcViewModel.enabledNfc()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED || intent.action == NfcAdapter.ACTION_TECH_DISCOVERED) {
            if (writableMode) {

            }
        }
    }
}
