package com.nohana.projetoiot.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nohana.projetoiot.model.database.entities.ListeningPointEntity
import com.nohana.projetoiot.model.database.relationships.ListeningPointAndDisease

@Dao
interface ListeningPointDao {
    @Insert suspend fun insertListeningPoint(listeningPoint: ListeningPointEntity): Long

    @Delete suspend fun deleteListeningPoint(listeningPoint: ListeningPointEntity)

    @Update suspend fun updateListeningPoint(listeningPoint: ListeningPointEntity)

    @Query("SELECT * FROM ListeningPointEntity")
    fun getAllListeningPoints(): LiveData<List<ListeningPointAndDisease>>

    @Query("SELECT * FROM ListeningPointEntity WHERE id=:id")
    fun getListeningPointById(id: Int): LiveData<ListeningPointAndDisease>

    @Query("SELECT * FROM ListeningPointEntity WHERE animalId=:animalId")
    suspend fun getPointByAnimalId(animalId: Int): List<ListeningPointAndDisease>
}