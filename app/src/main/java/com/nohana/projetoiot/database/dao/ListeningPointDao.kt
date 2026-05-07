package com.nohana.projetoiot.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nohana.projetoiot.database.entities.ListeningPointEntity
import com.nohana.projetoiot.database.relationships.ListeningPointWithScenarios

@Dao
interface ListeningPointDao {
    @Insert suspend fun insertListeningPoint(listeningPoint: ListeningPointEntity): Long

    @Delete suspend fun deleteListeningPoint(listeningPoint: ListeningPointEntity)

    @Update suspend fun updateListeningPoint(listeningPoint: ListeningPointEntity)

    @Query("SELECT * FROM ListeningPointEntity")
    fun getAllListeningPoints(): LiveData<List<ListeningPointWithScenarios>>

    @Query("SELECT * FROM ListeningPointEntity WHERE id=:id")
    fun getListeningPointById(id: Int): LiveData<ListeningPointWithScenarios>

    @Query("SELECT * FROM ListeningPointEntity WHERE animalId=:animalId")
    suspend fun getPointByAnimalId(animalId: Int): List<ListeningPointWithScenarios>
}