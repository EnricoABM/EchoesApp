package com.nohana.projetoiot.controller.animal

import android.content.Context
import com.nohana.projetoiot.model.animal.Animal
import com.nohana.projetoiot.database.AnimalDatabase
import com.nohana.projetoiot.database.repository.AnimalRepository
import kotlinx.coroutines.flow.Flow

class AnimalController(
    private val context: Context
) {

    private val animalRepository: AnimalRepository

    init {
        val db = AnimalDatabase.getInstance(context)
        animalRepository = AnimalRepository(
            animalDao = db.animalDao(),
            listeningPointDao = db.listeningPointDao(),
            diseaseDao = db.diseaseDao(),
            animalMapper = AnimalMapper(context)
        )

    }

    fun upsert(animal: Animal) {

    }

    fun getAnimalById() {

    }

    suspend fun updateActiveDiseases(animal: Animal) {
        animalRepository.updateListeningPoints(animal)
    }

    fun getAnimals(): Flow<List<Animal>> {
        return animalRepository.getAllAnimals()
    }

}