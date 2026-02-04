package com.nohana.projetoiot.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.nohana.projetoiot.database.dao.AnimalDao
import com.nohana.projetoiot.database.dao.DiseaseDao
import com.nohana.projetoiot.database.dao.ListeningPointDao
import com.nohana.projetoiot.database.entities.AnimalEntity
import com.nohana.projetoiot.database.entities.DiseaseEntity
import com.nohana.projetoiot.database.entities.ListeningPointEntity

@Database(
    entities = [AnimalEntity::class, ListeningPointEntity::class, DiseaseEntity::class],
    version = 2,
    exportSchema = false // Indica que o schema do banco de dados não deve ser exportado para o sistema de arquivos.
)
abstract class AnimalDatabase : RoomDatabase() {

    abstract fun animalDao(): AnimalDao

    abstract fun listeningPointDao(): ListeningPointDao

    abstract fun diseaseDao(): DiseaseDao

    companion object {

        @Volatile
        private var INSTANCE: AnimalDatabase? = null

       val MIGRATION_1_2 = Migration(1, 2) { database ->
            database.execSQL("CREATE TABLE new_animals (id integer not null, name text not null, imageUrl text not null, primary key (id))")
            database.execSQL("INSERT INTO new_animals (id, name, imageUrl) SELECT id, name, imageUrl FROM AnimalEntity")
            database.execSQL("DROP TABLE AnimalEntity")
            database.execSQL("ALTER TABLE new_animals RENAME TO AnimalEntity")
        }

        fun getInstance(context: Context): AnimalDatabase {
            // Se a instância já existir, retorna ela.
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            // Bloco sincronizado para garantir que apenas uma thread crie a instância.
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimalDatabase::class.java,
                    "banco.db"
                    ).createFromAsset("database/banco.db") // Cria o banco de dados a partir de um arquivo pré-populado na pasta assets.
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
