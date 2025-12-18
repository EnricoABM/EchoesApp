package com.nohana.projetoiot.model.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.nohana.projetoiot.model.database.entities.AnimalEntity
import com.nohana.projetoiot.model.database.entities.ListeningPointEntity

data class AnimalAndListeningPoint(
    @Embedded var animal: AnimalEntity,
    @Relation(
        entity = ListeningPointEntity::class,
        parentColumn = "id",
        entityColumn = "animalId"
    )
    var listeningPoints: List<ListeningPointAndDisease>
) {
}