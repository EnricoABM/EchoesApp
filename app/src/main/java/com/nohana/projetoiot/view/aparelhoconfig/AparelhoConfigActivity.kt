package com.nohana.projetoiot.view.aparelhoconfig

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.nohana.projetoiot.model.Animal
import com.nohana.projetoiot.R

//TODO solve the class with new classes
class AparelhoConfigActivity : AppCompatActivity() {
    var animais = mutableListOf<Animal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_aparelho_config)

        val button = findViewById<ImageButton>(R.id.tela_aparelho_voltar)
        button.setOnClickListener {
            finish()
        }
        this.initAnimais()
        this.initSpinners()
    }

    fun initSpinners() {

        // Tipo do Animal
        val animalSpinner = findViewById<Spinner>(R.id.tela_aparelho_tipo_animal)
        val nomes = mutableListOf<String>()
        nomes.add("Animal")
        nomes.addAll(animais.map{ it.name })

        val animalAdapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomes) {
            override fun isEnabled(position : Int) : Boolean {
                return position != 0
            }
        }

        animalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        animalSpinner.adapter = animalAdapter

        animalSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val itemSelecionado = parent.getItemAtPosition(position).toString()

                if (position != 0) {
                    loadPositionsOnScreen(itemSelecionado)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }

    fun initAnimais() {

        val a1 = Animal("Cachorro", R.drawable.cachorro)
        a1.newListeningPoint(0,"Coração")
        a1.newListeningPoint(0, "Pulmão")

        a1.addDiseaseToListeningPoint("Coração", "Normal", "teste")
        a1.addDiseaseToListeningPoint("Coração", "Aritmia", "teste")
        a1.addDiseaseToListeningPoint("Pulmão", "Normal", "teste")
        a1.addDiseaseToListeningPoint("Pulmão", "Pneumonia", "teste")

        animais.add(a1)

    }

    fun loadPositionsOnScreen(name : String) {
        val posicaoSpinner = findViewById<Spinner>(R.id.tela_aparelho_posicao)
        val animal = animais.first { it.name == name }

        val posicoes = mutableListOf<String>()
        posicoes.add("Posição")
        posicoes.addAll(animal.getListeningPoints().map { it.positionName })

        val posicaoAdapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, posicoes) {
            override fun isEnabled(position : Int) : Boolean {
                return position != 0
            }
        }

        posicaoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val itemSelecionado = parent.getItemAtPosition(position).toString()

                if (position != 0) {
                    loadIssuesOnScreen(itemSelecionado, animal)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        posicaoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        posicaoSpinner.adapter = posicaoAdapter

    }

    fun loadIssuesOnScreen(position : String, animal : Animal) {
        val spinnerDisease = findViewById<Spinner>(R.id.tela_aparelho_doenca)
        var points = animal.getListeningPoints()
        var point = points.first { it.positionName == position }

        val spinnerDiseases = mutableListOf<String>()
        val pointDiseases = point.getDiseases()
        spinnerDiseases.add("Doença")
        for (doenca in pointDiseases) {
            spinnerDiseases.add(doenca.name)
        }

        val adapterDisease = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerDiseases) {
            override fun isEnabled(position : Int) : Boolean {
                return position != 0
            }
        }

        adapterDisease.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDisease.adapter = adapterDisease
    }
}