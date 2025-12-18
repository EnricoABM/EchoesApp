package com.nohana.projetoiot.model.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.nohana.projetoiot.model.Disease
import com.nohana.projetoiot.model.ListeningPoint
import com.nohana.projetoiot.model.database.entities.DiseaseEntity
import com.nohana.projetoiot.model.database.entities.ListeningPointEntity

data class ListeningPointAndDisease(
    @Embedded var listeningPoint: ListeningPointEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "listeningPointId"
    )
    var diseases: List<DiseaseEntity>,
    @Relation(
        parentColumn = "activeDiseaseId",
        entityColumn = "id"
    )
    var activeDisease: DiseaseEntity?
) {
}