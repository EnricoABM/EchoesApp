package com.nohana.projetoiot.controller

import com.nohana.projetoiot.model.Animal
import com.nohana.projetoiot.R

class AnimalController {
    var animals: MutableList<Animal> = mutableListOf()

    constructor() {
        initAnimais()
    }

    fun initAnimais() {

        val a1 = Animal("Cachorro", R.drawable.cachorro)
        a1.newListeningPoint(0, "Coração")
        a1.newListeningPoint(0, "Pulmão")

        a1.addDiseaseToListeningPoint("Coração", "Normal", "https://drive.google.com/uc?export=download&id=1TuMs5UDL_bxoCy0N2NYVx7tnKaORwfx7")
        a1.addDiseaseToListeningPoint("Coração", "Aritmia", "teste")
        a1.addDiseaseToListeningPoint("Pulmão", "Normal", "https://drive.google.com/uc?export=download&id=1TuMs5UDL_bxoCy0N2NYVx7tnKaORwfx7 ")
        a1.addDiseaseToListeningPoint("Pulmão", "Pneumonia", "teste")

        a1.setActiveDiseaseToListeningPoint("Coração", "Normal")
        a1.setActiveDiseaseToListeningPoint("Pulmão", "Normal")


    }

    companion object  {
        private var animal : Animal? = null

        fun setActiveAnimal(new: Animal) {
            animal = new
        }

        fun getActiveAnimal(): Animal? {
            if (animal == null) {
                val a1 = Animal("Gato", R.drawable.gato)
                a1.newListeningPoint(0, "Coração")
                a1.newListeningPoint(0, "Pulmão")

                a1.addDiseaseToListeningPoint("Coração", "Normal", "https://drive.google.com/uc?export=download&id=1TuMs5UDL_bxoCy0N2NYVx7tnKaORwfx7")
                a1.addDiseaseToListeningPoint("Coração", "Aritmia", "https://drive.google.com/uc?export=download&id=1XUR2pMBc67H-eJVENF0CySON5YMGSpoX")
                a1.addDiseaseToListeningPoint("Pulmão", "Normal", "https://drive.google.com/uc?export=download&id=1TuMs5UDL_bxoCy0N2NYVx7tnKaORwfx7")
                a1.addDiseaseToListeningPoint("Pulmão", "Pneumonia", "https://drive.google.com/uc?export=download&id=1XUR2pMBc67H-eJVENF0CySON5YMGSpoX")

                a1.setActiveDiseaseToListeningPoint("Coração", "Normal")
                a1.setActiveDiseaseToListeningPoint("Pulmão", "Normal")
                animal = a1
            }
            return animal
        }

    }

}