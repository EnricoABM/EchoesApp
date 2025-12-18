package com.nohana.projetoiot.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nohana.projetoiot.model.database.entities.AnimalEntity
import com.nohana.projetoiot.model.database.relationships.AnimalAndListeningPoint

@Dao
interface AnimalDao {
    @Insert suspend fun insertAnimal(animal: AnimalEntity): Long

    @Delete suspend fun deleteAnimal(animal: AnimalEntity)

    @Update suspend fun updateAnimal(animal: AnimalEntity)

    @Query("SELECT * FROM AnimalEntity")
    fun getAllAnimals(): LiveData<List<AnimalAndListeningPoint>>

    @Query("SELECT id FROM AnimalEntity WHERE name=:name")
    suspend fun getAnimalIdByName(name: String): Int

    @Query("SELECT * FROM AnimalEntity WHERE id=:id")
    fun getAnimalById(id: Int): LiveData<AnimalAndListeningPoint>

    @Query("SELECT * FROM AnimalEntity WHERE name=:name")
    fun getAnimalByName(name: String): LiveData<AnimalAndListeningPoint>

    @Transaction
    @Query("SELECT * FROM AnimalEntity WHERE name=:name")
    suspend fun getAnimalByNameSusp(name: String?): AnimalAndListeningPoint?

    @Query("SELECT * FROM AnimalEntity")
    suspend fun getAnimalList(): List<AnimalAndListeningPoint>

}