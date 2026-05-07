package com.nohana.projetoiot.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ListeningPointEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0, // O valor padrão 0 é usado antes da inserção, quando o ID ainda não foi gerado.
    var position: String,
    var animalId: Int,
    var activeScenarioId: Int? = null
) {
}

