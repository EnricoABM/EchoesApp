package com.nohana.projetoiot.model.database.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.nohana.projetoiot.model.Animal
import com.nohana.projetoiot.model.database.dao.DiseaseDao
import com.nohana.projetoiot.model.database.dao.ListeningPointDao
import com.nohana.projetoiot.model.database.dao.AnimalDao
import com.nohana.projetoiot.model.database.entities.AnimalEntity
import com.nohana.projetoiot.model.database.entities.DiseaseEntity
import com.nohana.projetoiot.model.database.entities.ListeningPointEntity
import com.nohana.projetoiot.model.database.relationships.AnimalAndListeningPoint
import com.nohana.projetoiot.model.database.relationships.ListeningPointAndDisease

class AnimalRepository(
    private val animalDao: AnimalDao,
    private val listeningPointDao: ListeningPointDao,
    private val diseaseDao: DiseaseDao) {

    private val animalsLiveData = animalDao.getAllAnimals()

    suspend fun insert(animal: Animal) {
        val animalEntity = AnimalEntity(
            name = animal.name,
            imageUrl = animal.imageUrl
        )
        val animalId = animalDao.insertAnimal(animalEntity)
        val listeningPoints = animal.getListeningPoints()

        for (point in listeningPoints) {
            val listeningPointEntity = ListeningPointEntity(
                position = point.positionName,
                animalId = animalId.toInt()
            )
            val pointId = listeningPointDao.insertListeningPoint(listeningPointEntity)

            val diseases = point.getDiseases()
            for (disease in diseases) {
                val diseaseEntity = DiseaseEntity(
                    name = disease.name,
                    audioUrl = disease.audioUrl,
                    listeningPointId = pointId.toInt()
                )
                val diseaseId = diseaseDao.insertDisease(diseaseEntity)
                if (point.getActiveDisease() != null && point.getActiveDisease()?.name == disease.name) {
                    val updatedPoint = listeningPointEntity.copy(activeDiseaseId = diseaseId.toInt())
                    listeningPointDao.updateListeningPoint(updatedPoint)
                }
            }
        }
    }

    fun getAllAnimals(): LiveData<List<Animal>> {

        // Transformando o LiveData do Relacionamento em um com Animais completos
        val liveAnimals = animalsLiveData.map { listAnimalsEntity ->
            // Transformando a lista para Animais completos
            listAnimalsEntity.map { animalEntity ->
                val animal = animalEntityToAnimal(animalEntity)
                animal
            }
        }

        return liveAnimals
    }

    fun getAnimalByName(name : String): LiveData<Animal> {
        val entity = animalDao.getAnimalByName(name)

        val livedata = entity.map { entity ->
            val animal = Animal(
                entity.animal.name,
                entity.animal.imageUrl
            )

            for (point in entity.listeningPoints) {
                animal.newListeningPoint(point.listeningPoint.id, point.listeningPoint.position)

                val diseases = point.diseases
                for (disease in diseases) {
                    animal.addDiseaseToListeningPoint(
                        point.listeningPoint.position,
                        disease.name,
                        disease.audioUrl,
                        disease.id
                    )

                    if (point.listeningPoint.activeDiseaseId == disease.id) {
                        animal.setActiveDiseaseToListeningPoint(point.listeningPoint.position, disease.name)
                    }
                }
            }

            animal
        }
        return livedata
    }

    fun animalEntityToAnimal(entity: AnimalAndListeningPoint): Animal {
        val animal = Animal(
            entity.animal.name,
            entity.animal.imageUrl
        )
        animal.id = entity.animal.id

        for (point in entity.listeningPoints) {
            animal.newListeningPoint(point.listeningPoint.id,point.listeningPoint.position)

            val listDiseases = point.diseases
            for (disease in listDiseases) {
                animal.addDiseaseToListeningPoint(
                    point.listeningPoint.position,
                    disease.name,
                    disease.audioUrl,
                    disease.id
                )

            }

            val activeDiseaseId = point.activeDisease?.id
            val activeDisease = listDiseases.find { it.id == activeDiseaseId }
            if (activeDisease != null)
                animal.setActiveDiseaseToListeningPoint(point.listeningPoint.position, activeDisease.name)
        }
        return animal
    }

    suspend fun updateActiveDiseases(animal: Animal, id: Int, relationship: List<ListeningPointAndDisease>) {

        // Alterar os dados dentro do ponto para a doença correta

        // Coleto os pontos do animal
        val points = animal.getListeningPoints()

        for (entities in relationship) {
            // Busca o ponto relacionado a iteração
            val point = points.first { it.positionName == entities.listeningPoint.position }
            Log.d("DEBUG", "Pontos: ${point.positionName} == ${entities.listeningPoint.position}")

            // Pega a doença selecionada
            val disease = point.getActiveDisease()
            Log.d("DEBUG", "Doença: ${disease?.id} - ${disease?.name}")

            // Pega o registro do banco da doença
            val diseaseEntity = entities.diseases.first{ it.name == disease?.name }
            Log.d("DEBUG", "Registro Doença: ${diseaseEntity.id} - ${diseaseEntity.name}")

            // Pegar dados para entity
            val diseaseId = diseaseEntity.id

            // Alterar dados para salvar ao banco
            val pointEntity = entities.listeningPoint
            pointEntity.activeDiseaseId = diseaseId
            Log.d("DEBUG", "Entity a ser salva: ${pointEntity.id} - ${pointEntity.position} - ${pointEntity.animalId} - ${pointEntity.activeDiseaseId}")

            listeningPointDao.updateListeningPoint(pointEntity)
        }
    }

    suspend fun getAnimalIdByName(name: String): Int {
        return animalDao.getAnimalIdByName(name)
    }

    suspend fun getPointByAnimalId(animalId: Int): List<ListeningPointAndDisease> {
        return listeningPointDao.getPointByAnimalId(animalId)
    }

    suspend fun getAnimal(animal: String?): Animal? {

        val ani = animalDao.getAnimalByNameSusp(animal)
        return ani?.let { this.animalEntityToAnimal(it) }
    }

    suspend fun getListAnimal(): List<AnimalAndListeningPoint> {
        return animalDao.getAnimalList()
    }

}