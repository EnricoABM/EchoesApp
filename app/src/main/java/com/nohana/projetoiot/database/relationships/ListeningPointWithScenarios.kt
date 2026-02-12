package com.nohana.projetoiot.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.nohana.projetoiot.database.entities.ScenarioEntity
import com.nohana.projetoiot.database.entities.ListeningPointEntity

data class ListeningPointAndDisease(
    @Embedded var listeningPoint: ListeningPointEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "listeningPointId"
    )
    var diseases: List<ScenarioEntity>,
    @Relation(
        parentColumn = "activeDiseaseId",
        entityColumn = "id"
    )
    var activeDisease: ScenarioEntity?
) {
}