package com.nohana.projetoiot.controller.animal

import android.content.Context
import com.nohana.projetoiot.model.animal.Animal
import com.nohana.projetoiot.model.animal.ListeningPoint
import com.nohana.projetoiot.database.relationships.AnimalWithListeningPoint
import com.nohana.projetoiot.model.animal.Scenario

class AnimalMapper(
    private val context: Context
) {
    fun entityToModel(entity: AnimalWithListeningPoint): Animal {
        val resId = context.resources.getIdentifier(
            entity.animal.imageUrl,
            "drawable",
            context.packageName
        )

        val points = entity.listeningPoints.map { pointRelation ->
            val scenariosEntities = pointRelation.scenarios
            val scenarios = scenariosEntities.map { scenarioEntity ->
                Scenario(
                    id = scenarioEntity.id,
                    name = scenarioEntity.name,
                    audioUrl = scenarioEntity.audioUrl
                )
            }
            val activeScenario = scenarios.find { it.id ==  pointRelation.activeScenario?.id}
            ListeningPoint(
                id = pointRelation.listeningPoint.id,
                positionName = pointRelation.listeningPoint.position,
                scenarios = scenarios,
                activeScenario = activeScenario
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