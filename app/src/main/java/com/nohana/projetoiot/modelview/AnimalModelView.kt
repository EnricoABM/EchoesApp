package com.nohana.projetoiot.modelview

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.nohana.projetoiot.model.Animal
import com.example.projetoic.service.DiseaseMidiaPlayer
import com.nohana.projetoiot.model.TagNfc
import com.nohana.projetoiot.model.database.AnimalDatabase
import com.nohana.projetoiot.model.database.relationships.AnimalAndListeningPoint
import com.nohana.projetoiot.model.database.repository.AnimalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnimalModelView(application: Application): AndroidViewModel(application) {

    private var _animalLiveData = MutableLiveData<List<Animal>>()
    val animalLiveData: LiveData<List<Animal>> get() = _animalLiveData
    private var animalRepository: AnimalRepository

    private val player = DiseaseMidiaPlayer()


    init {
        val db = AnimalDatabase.getInstance(application)
        animalRepository = AnimalRepository(db.animalDao(), db.listeningPointDao(), db.diseaseDao())
        _animalLiveData = animalRepository.getAllAnimals() as MutableLiveData<List<Animal>>
    }

    fun addAnimal(animal: Animal) {
        viewModelScope.launch {
            animalRepository.insert(animal)
        }
    }

    fun findAnimalByName(name: String): LiveData<Animal> {
        return animalRepository.getAnimalByName(name)
    }

    fun updateAnimal(name: String, options: MutableMap<String, String>) {
        val animalLiveData = findAnimalByName(name)



        val observer = object : Observer<Animal> {
            override fun onChanged(animal: Animal) {
                if (animal != null) {
                    for ((position, diseaseName) in options) {
                        animal.setActiveDiseaseToListeningPoint(position, diseaseName)
                    }

                    viewModelScope.launch {
                        val animalId = animalRepository.getAnimalIdByName(name)

                        val pointsEntity = animalRepository.getPointByAnimalId(animalId)
                        Log.d("DEBUG", animal.imageUrl.toString())
                        animalRepository.updateActiveDiseases(animal, animalId, pointsEntity)
                    }


                }

                animalLiveData.removeObserver(this)
            }

        }
        animalLiveData.observeForever(observer)

    }

//    fun getAnimalByName(animal: String?): Animal {
//        var ani = Animal("", 0)
//        viewModelScope.launch {
//             ani = animalRepository.getAnimal(animal)
//        }
//        return ani
//    }

    fun getAnimalsList(): List<AnimalAndListeningPoint> {
        var list = mutableListOf<AnimalAndListeningPoint>()
        viewModelScope.launch {
            list.addAll(animalRepository.getListAnimal())
        }
        return list
    }

    fun play(tag: TagNfc?) {
        viewModelScope.launch {
            if (tag != null) {
                val animal = withContext(Dispatchers.IO) {
                    animalRepository.getAnimal(tag.animal)
                }

                Log.d("DEBUG", "OLA")
                val listeningPoint = animal?.getListeningPointByPosition(tag?.position ?: "")
                val disease = listeningPoint?.getActiveDisease()
                player.play(disease)
            }

        }
    }

    fun startAndPauseMediaPlayer(): Boolean {
        return player.startOrPause()
    }

    fun stopMediaPlayer() {
        player.stop()
    }

}