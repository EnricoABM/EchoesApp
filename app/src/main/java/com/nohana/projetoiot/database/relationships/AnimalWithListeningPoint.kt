package com.nohana.projetoiot.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.nohana.projetoiot.database.entities.AnimalEntity
import com.nohana.projetoiot.database.entities.ListeningPointEntity

data class AnimalWithListeningPoint(
    @Embedded var animal: AnimalEntity,
    @Relation(
        entity = ListeningPointEntity::class,
        parentColumn = "id",
        entityColumn = "animalId"
    )
    var listeningPoints: List<ListeningPointWithScenarios>
) {
}