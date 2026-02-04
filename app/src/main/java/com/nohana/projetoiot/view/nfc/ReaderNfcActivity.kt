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
//import com.nohana.projetoiot.model.TagNfc
//import com.nohana.projetoiot.view.nfc.writer.WriterNfcActivity
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
//    private lateinit var animalMH: AnimalModelView
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
//        animalMH = ViewModelProvider(this)[AnimalModelView::class.java]
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
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action || NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
//            var tag = read(intent)
//
//            Toast.makeText(this, tag.toString(), Toast.LENGTH_LONG).show()
//            animalMH.play(tag)
//
//        }
//    }
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
//    fun configNfc() {
//        if (nfcAdapter == null) {
//            messageTxt.text = "NFC não suportado"
//        } else if (!nfcAdapter!!.isEnabled) {
//            messageTxt.text = "NFC desligado"
//        }
//
//        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            // PendingIntent NFC acima da versão 13 precisam receber a Flag MUTABLE
//            pi = PendingIntent.getActivity(this, 0, intent, android.app.PendingIntent.FLAG_MUTABLE)
//        } else {
//            pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT);
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
//    }
//
//    fun read(intent: Intent): TagNfc? {
//        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return null
//        val ndef = Ndef.get(tag) ?: return null
//
//        var tagNfc: TagNfc? = null
//        try {
//            ndef.connect()
//            val ndefMessage = ndef.cachedNdefMessage
//            val records = ndefMessage.records
//
//            if (records.isNotEmpty()) {
//                val animal = String(records[0].payload).drop(3)
//                val position = String(records[1].payload).drop(3)
//
//                tagNfc = TagNfc(animal, position)
//            }
//
//            ndef.close()
//        } catch (e: Exception) {
//            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
//        }
//        return tagNfc
//    }
//}