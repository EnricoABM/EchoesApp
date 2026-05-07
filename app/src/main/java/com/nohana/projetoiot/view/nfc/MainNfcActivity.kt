package com.nohana.projetoiot.view.nfc

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareUltralight
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.nfc.tech.NfcA
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.nohana.projetoiot.R
import com.nohana.projetoiot.model.animal.Animal
import com.nohana.projetoiot.model.nfc.TagNfc
import com.nohana.projetoiot.view.components.AnimalConfigScreen
import com.nohana.projetoiot.view.components.TitleHeader
import com.nohana.projetoiot.view.ui.theme.ProjetoIotTheme
import com.nohana.projetoiot.viewmodel.AnimalViewModel
import com.nohana.projetoiot.viewmodel.factory.AnimalViewModelFactory
import com.nohana.projetoiot.viewmodel.factory.NfcViewModelFactory
import kotlin.getValue

class MainNfcActivity : ComponentActivity() {

    private val animalViewModel: AnimalViewModel by viewModels {
        AnimalViewModelFactory(
            applicationContext
        )
    }
    private val nfcAdapter : NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    private lateinit var pendingIntent: PendingIntent
    private lateinit var techList: Array<Array<String>>
    private lateinit var intentFilter: Array<IntentFilter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.configNfc()

        setContent {
            ProjetoIotTheme {
                MainNfcScreen(
                    animalViewModel = animalViewModel,
                    onWriteRequested = { animal ->
                        // Recebe animal configurado e habilita modo escrita
                        Toast.makeText(this, "Aproxime a Tag para gravar", Toast.LENGTH_LONG).show()
                    },
                    onPlayPause = { audioUrl ->

                    },
                    onStop = {

                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(
            this@MainNfcActivity,
            pendingIntent,
            intentFilter,
            techList
        )
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this@MainNfcActivity)
        // nfcViewModel.stopPlayback()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        Toast.makeText(this, intent.toString(), Toast.LENGTH_LONG).show()
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action || NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
            val tag = readTag(intent)

            Toast.makeText(this, tag.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun writeTag(intent: Intent) {

    }

    private fun readTag(intent: Intent): TagNfc? {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return null
        val ndef = Ndef.get(tag) ?: return null

        var tagNfc: TagNfc? = null
        try {
            ndef.connect()
            val ndefMessage = ndef.cachedNdefMessage
            val records = ndefMessage.records

            if (records.isNotEmpty()) {
                val animal = String(records[0].payload).drop(3)
                val position = String(records[1].payload).drop(3)

                tagNfc = TagNfc(animal, position)
            }

            ndef.close()
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
        return tagNfc
    }

    fun configNfc() {
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC não suportado", Toast.LENGTH_LONG).show()
        } else if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(this, "NFC Desligado", Toast.LENGTH_LONG).show()
        }

        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // PendingIntent NFC acima da versão 13 precisam receber a Flag MUTABLE
            pendingIntent = PendingIntent.getActivity(this, 0, intent, android.app.PendingIntent.FLAG_MUTABLE)
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT);
        }

        val ndefFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val tagFilter = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        val techFilter = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)

        try {
            ndefFilter.addDataType("*/*")
            tagFilter.addDataType("*/*")
            techFilter.addDataType("*/*")
        } catch (e : IntentFilter.MalformedMimeTypeException) {
            throw RuntimeException("Erro")
        }

        intentFilter = arrayOf<IntentFilter> (
            ndefFilter,
            tagFilter,
            techFilter
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNfcScreen(
    animalViewModel: AnimalViewModel,
    onWriteRequested: (Animal) -> Unit,
    onPlayPause: (String) -> Unit,
    onStop: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Leitura", "Escrita")

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = { TitleHeader("NFC") })
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> ReaderTab(
                modifier = Modifier.padding(innerPadding),
                onPlayPause = onPlayPause,
                onStop = onStop
            )
            1 -> AnimalConfigScreen(
                viewModel = animalViewModel,
                onConfirm = { animal -> animal?.let { onWriteRequested(it) } }
            )
        }
    }
}

@Composable
fun ReaderTab(
    modifier: Modifier = Modifier,
    onPlayPause: (String) -> Unit,
    onStop: () -> Unit
) {
    // audioUrl da tag lida — em produção viria de um StateFlow do ViewModel
    var currentAudioUrl by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.imagem_tela_leitor),
            contentDescription = "Leitor NFC",
            modifier = Modifier.size(160.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text("Aproxime uma Tag NFC para ler")

        Spacer(modifier = Modifier.height(40.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { onPlayPause(currentAudioUrl) }) {
                Icon(
                    painter = painterResource(R.drawable.icone_play),
                    contentDescription = "Play/Pause"
                )
            }
            OutlinedButton(onClick = onStop) {
                Text("Parar")
            }
        }
    }
}