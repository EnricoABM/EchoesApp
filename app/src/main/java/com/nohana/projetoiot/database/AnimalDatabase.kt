package com.nohana.projetoiot.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nohana.projetoiot.database.dao.AnimalDao
import com.nohana.projetoiot.database.dao.ListeningPointDao
import com.nohana.projetoiot.database.dao.ScenarioDao
import com.nohana.projetoiot.database.entities.AnimalEntity
import com.nohana.projetoiot.database.entities.ListeningPointEntity
import com.nohana.projetoiot.database.entities.ScenarioEntity

@Database(
    entities = [
        AnimalEntity::class,
        ListeningPointEntity::class,
        ScenarioEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AnimalDatabase : RoomDatabase() {

    abstract fun animalDao(): AnimalDao
    abstract fun listeningPointDao(): ListeningPointDao
    abstract fun scenarioDao(): ScenarioDao

    companion object {
        @Volatile
        private var INSTANCE: AnimalDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `newanimals` (
                        `id` INTEGER NOT NULL,
                        `name` TEXT NOT NULL,
                        `imageUrl` TEXT NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                """.trimIndent())

                database.execSQL("""
                    INSERT INTO `newanimals` (`id`, `name`, `imageUrl`)
                    SELECT `id`, `name`, `imageUrl`
                    FROM `AnimalEntity`
                """.trimIndent())

                database.execSQL("DROP TABLE `AnimalEntity`")
                database.execSQL("ALTER TABLE `newanimals` RENAME TO `AnimalEntity`")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `ListeningPointEntity_new` (
                        `id` INTEGER NOT NULL,
                        `position` TEXT NOT NULL,
                        `animalId` INTEGER NOT NULL,
                        `activeScenarioId` INTEGER,
                        PRIMARY KEY(`id`)
                    )
                """.trimIndent())

                database.execSQL("""
                    INSERT INTO `ListeningPointEntity_new`
                    (`id`, `position`, `animalId`, `activeScenarioId`)
                    SELECT `id`, `position`, `animalId`, `activeScenarioId`
                    FROM `ListeningPointEntity`
                """.trimIndent())

                database.execSQL("DROP TABLE `ListeningPointEntity`")
                database.execSQL("ALTER TABLE `ListeningPointEntity_new` RENAME TO `ListeningPointEntity`")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `ScenarioEntity` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `name` TEXT NOT NULL,
                        `audioUrl` TEXT NOT NULL,
                        `listeningPointId` INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }

        fun getInstance(context: Context): AnimalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimalDatabase::class.java,
                    "banco.db"
                )
                    .createFromAsset("database/banco.db")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}