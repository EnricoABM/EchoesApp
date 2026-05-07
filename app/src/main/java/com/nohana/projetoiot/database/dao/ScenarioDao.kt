package com.nohana.projetoiot.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nohana.projetoiot.database.entities.ScenarioEntity

@Dao
interface ScenarioDao {
    @Insert suspend fun insertDisease(disease: ScenarioEntity): Long

    @Delete suspend fun deleteDisease(disease: ScenarioEntity)

    @Update suspend fun updateDisease(disease: ScenarioEntity)

    @Query("SELECT * FROM ScenarioEntity")
    fun getAllDiseases(): LiveData<List<ScenarioEntity>>

    @Query("SELECT * FROM ScenarioEntity WHERE id=:id")
    fun getDiseaseById(id: Int): LiveData<ScenarioEntity>

    @Query("SELECT * FROM ScenarioEntity WHERE listeningPointId=:id")
    fun getDiseaseByListeningPointId(id: Int): List<ScenarioEntity>
}