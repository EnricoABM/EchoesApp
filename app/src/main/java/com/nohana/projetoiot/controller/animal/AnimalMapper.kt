package com.nohana.projetoiot.controller.animal

import android.content.Context
import com.nohana.projetoiot.model.Animal
import com.nohana.projetoiot.model.Disease
import com.nohana.projetoiot.model.ListeningPoint
import com.nohana.projetoiot.database.relationships.AnimalAndListeningPoint

class AnimalMapper(
    private val context: Context
) {
    fun entityToModel(entity: AnimalAndListeningPoint): Animal {
        val resId = context.resources.getIdentifier(
            entity.animal.imageUrl,
            "drawable",
            context.packageName
        )

        val points = entity.listeningPoints.map { pointRelation ->
            val diseasesEntities = pointRelation.diseases
            val diseases = diseasesEntities.map { diseaseEntity ->
                Disease(
                    id = diseaseEntity.id,
                    name = diseaseEntity.name,
                    audioUrl = diseaseEntity.audioUrl
                )
            }
            val activeDisease = diseases.find { it.id ==  pointRelation.activeDisease?.id}
            ListeningPoint(
                id = pointRelation.listeningPoint.id,
                positionName = pointRelation.listeningPoint.position,
                diseases = diseases,
                activeDisease = activeDisease
            )
        }

        return Animal(
            id = entity.animal.id,
            name = entity.animal.name,
            imageUrl = resId,
            listeningPoints = points
        )

    }
//
//    fun modelToEntity(animal: Animal): AnimalEntity {
//
//    }
}