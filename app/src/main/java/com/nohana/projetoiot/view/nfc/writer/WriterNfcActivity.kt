package com.nohana.projetoiot.view.nfc.writer

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.nohana.projetoiot.R
import com.nohana.projetoiot.model.TagNfc
import com.nohana.projetoiot.viewmodel.AnimalModelView

class WriterNfcActivity : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var animalSpinner: Spinner
    private lateinit var image: ImageView
    private lateinit var posSpinner: Spinner
    private lateinit var writeButton: Button
    private lateinit var animalMV: AnimalModelView

    // Filtro de Intents, representa quais intents serão lidas
    private var intentFiltersArray: Array<IntentFilter>? = null

    // Filtro de Tecnologias, representa quais tecnologias serão lidas
    private val techListsArray = arrayOf(arrayOf(Ndef::class.java.name))

    // Representa a intenção vinda do sistema NFC.
    private var pendingIntent: PendingIntent? = null
    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }
    private var tagNfc: TagNfc? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_writer)

        initComponents()
        configSpinners()
        configButton()
    }

    private fun initComponents() {
        backButton = findViewById<ImageButton>(R.id.tela_nfc_gravar_voltar)
        animalSpinner = findViewById<Spinner>(R.id.tela_nfc_gravar_animal)
        image = findViewById<ImageButton>(R.id.tela_nfc_gravar_imagem)
        posSpinner = findViewById<Spinner>(R.id.tela_nfc_gravar_posicao)
        writeButton = findViewById<Button>(R.id.tela_nfc_gravar_botao_gravar)
        animalMV = ViewModelProvider(this)[AnimalModelView::class.java]

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun configSpinners() {
        // Configura o adapter do spinner, inicializando com uma lista vazia
        val spinnerAdapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListOf<String>()) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }
        }

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        animalSpinner.adapter = spinnerAdapter


        // Observa as mudanças na lista de animais para atualizar o spinner
        animalMV.animalLiveData.observe(this) {animals ->
            val names = animals.map { it.name }.toMutableList()
            names.add(0, "Animal")
            spinnerAdapter.clear()
            spinnerAdapter.addAll(names)
        }

        // Listener para seleção de itens no spinner
        animalSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val name = parent.getItemAtPosition(position) as String
                if (position != 0) {
                    loadListeningPoints(name)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }


    }

    // Carrega os pontos de escuta associados a um animal
    private fun loadListeningPoints(name: String) {

        // Config do Adapter
        val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item)
        val livedata = animalMV.findAnimalByName(name)

        livedata.observe(this) { animal ->
            image.setImageResource(animal.imageUrl)
            val names = animal.getListeningPoints().map { it.positionName }
            spinnerAdapter.clear()
            spinnerAdapter.addAll(names)
        }
        posSpinner.adapter = spinnerAdapter

    }

    private fun configButton() {
        configNfc()
        writeButton.setOnClickListener {
            val animal = animalSpinner.selectedItem as String
            val point = posSpinner.selectedItem as String

            tagNfc = TagNfc(animal, point)
            Toast.makeText(this, "Aproxime a Tag", Toast.LENGTH_LONG).show()
        }
    }

    private fun configNfc() {
        // prepare pending Intent
        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        }

        val ndef = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        intentFiltersArray = arrayOf(ndef)

        // Verifica disponibilidadedo NFC
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC não suportado", Toast.LENGTH_SHORT).show()
        } else if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(this, "NFC desligado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        try {
            if(tagNfc != null) {
                if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
                    || NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                ) {
                    val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
                    val ndef = Ndef.get(tag) ?: return

                    if (ndef.isWritable) {
                        val nfcMessage = NdefMessage(
                            arrayOf(
                                NdefRecord.createTextRecord("en", tagNfc!!.animal),
                                NdefRecord.createTextRecord("en", tagNfc!!.position)
                            )
                        )

                        ndef.connect()
                        ndef.writeNdefMessage(nfcMessage)
                        ndef.close()

                        Toast.makeText(applicationContext, "Dados gravados com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        catch (e:Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray)
    }

    override fun onPause() {
        if (this.isFinishing)
            nfcAdapter?.disableForegroundDispatch(this)
        super.onPause()
    }
}