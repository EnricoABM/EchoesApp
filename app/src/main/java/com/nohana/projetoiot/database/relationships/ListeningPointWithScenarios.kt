package com.nohana.projetoiot.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.nohana.projetoiot.database.entities.ScenarioEntity
import com.nohana.projetoiot.database.entities.ListeningPointEntity

data class ListeningPointWithScenarios(
    @Embedded var listeningPoint: ListeningPointEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "listeningPointId"
    )
    var scenarios: List<ScenarioEntity>,
    @Relation(
        parentColumn = "activeScenarioId",
        entityColumn = "id"
    )
    var activeScenario: ScenarioEntity?
) {
}