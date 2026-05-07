//package com.nohana.projetoiot.view.nfc
//
//import android.app.PendingIntent
//import android.content.Intent
//import android.content.IntentFilter
//import android.nfc.NfcAdapter
//import android.nfc.Tag
//import android.nfc.tech.Ndef
//import android.os.Build
//import android.os.Bundle
//import android.widget.Button
//import android.widget.ImageButton
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.ViewModelProvider
//import com.nohana.projetoiot.R
//import com.nohana.projetoiot.model.nfc.TagNfc
//import com.nohana.projetoiot.view.nfc.writer.WriterNfcActivity
//import com.nohana.projetoiot.viewmodel.AnimalViewModel
//import kotlin.jvm.java
//
//class ReaderNfcActivity : AppCompatActivity() {
//    private val nfcAdapter : NfcAdapter? by lazy {
//        NfcAdapter.getDefaultAdapter(this)
//    }
//    // private var player = DiseaseMidiaPlayer()
//    private var intentFilterArray : Array<IntentFilter>? = null
//    private var pi: PendingIntent? = null
//    private lateinit var messageTxt: TextView
//    private lateinit var configActivityButton: Button
//    private lateinit var writeActivityButton: Button
//    private lateinit var playButton: ImageButton
//    private lateinit var animalMH: AnimalViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContentView(R.layout.activity_nfc_leitor)
//
//        messageTxt = findViewById<TextView>(R.id.tela_leitor_nfc_mensagem)
//        configActivityButton = findViewById<Button>(R.id.tela_leitor_nfc_botao_config)
//        writeActivityButton = findViewById<Button>(R.id.tela_leitor_nfc_botao_gravar)
//        playButton = findViewById<ImageButton>(R.id.tela_leitor_nfc_play)
//        animalMH = ViewModelProvider(this)[AnimalViewModel::class.java]
//
//        playButton.setOnClickListener {
//            var isPlaying = animalMH.startAndPauseMediaPlayer()
//            if (isPlaying) {
//                Toast.makeText(this, "Reproduzindo", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Pausado", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        configActivityButton.setOnClickListener {
//            var intent = Intent(this, ConfigNfcActivity::class.java)
//            startActivity(intent)
//        }
//
//        writeActivityButton.setOnClickListener {
//            var intent = Intent(this, WriterNfcActivity::class.java)
//            startActivity(intent)
//        }
//
//
//        // Start NFC
//        this.configNfc()
//    }
//

//
//    override fun onResume() {
//        super.onResume()
//
//        nfcAdapter?.enableForegroundDispatch(this, pi, intentFilterArray, null)
//    }
//
//    override fun onPause() {
//        super.onPause()
//
//        nfcAdapter?.disableForegroundDispatch(this)
//        animalMH.stopMediaPlayer()
//    }
//

//
//}