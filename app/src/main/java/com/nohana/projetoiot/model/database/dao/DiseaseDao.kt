package com.nohana.projetoiot.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nohana.projetoiot.model.database.entities.DiseaseEntity

@Dao
interface DiseaseDao {
    @Insert suspend fun insertDisease(disease: DiseaseEntity): Long

    @Delete suspend fun deleteDisease(disease: DiseaseEntity)

    @Update suspend fun updateDisease(disease: DiseaseEntity)

    @Query("SELECT * FROM DiseaseEntity")
    fun getAllDiseases(): LiveData<List<DiseaseEntity>>

    @Query("SELECT * FROM DiseaseEntity WHERE id=:id")
    fun getDiseaseById(id: Int): LiveData<DiseaseEntity>

    @Query("SELECT * FROM DiseaseEntity WHERE listeningPointId=:id")
    fun getDiseaseByListeningPointId(id: Int): List<DiseaseEntity>
}