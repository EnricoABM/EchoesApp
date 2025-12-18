package com.nohana.projetoiot.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
// Corrigindo os imports para refletir a estrutura da classe
import com.nohana.projetoiot.model.database.dao.AnimalDao
import com.nohana.projetoiot.model.database.dao.DiseaseDao
import com.nohana.projetoiot.model.database.dao.ListeningPointDao
import com.nohana.projetoiot.model.database.entities.AnimalEntity
import com.nohana.projetoiot.model.database.entities.DiseaseEntity
import com.nohana.projetoiot.model.database.entities.ListeningPointEntity
// Import do R não é necessário aqui para a documentação da classe em si,
// a menos que seja usado diretamente nela (o que não é o caso).

/**
 * Classe principal do banco de dados Room para o aplicativo.
 *
 * Esta classe abstrata define a configuração do banco de dados, incluindo as entidades
 * que ele gerencia e fornece acesso aos Data Access Objects (DAOs).
 *
 * As entidades incluídas são [AnimalEntity], [ListeningPointEntity] e [DiseaseEntity].
 * A versão atual do banco de dados é 1. O schema não é exportado.
 */
@Database(
    entities = [AnimalEntity::class, ListeningPointEntity::class, DiseaseEntity::class],
    version = 1,
    exportSchema = false // Indica que o schema do banco de dados não deve ser exportado para o sistema de arquivos.
)
abstract class AnimalDatabase : RoomDatabase() {

    /**
     * Fornece acesso ao Data Access Object (DAO) para operações relacionadas a [AnimalEntity].
     * @return Uma instância de [AnimalDao].
     */
    abstract fun animalDao(): AnimalDao

    /**
     * Fornece acesso ao Data Access Object (DAO) para operações relacionadas a [ListeningPointEntity].
     * @return Uma instância de [ListeningPointDao].
     */
    abstract fun listeningPointDao(): ListeningPointDao

    /**
     * Fornece acesso ao Data Access Object (DAO) para operações relacionadas a [DiseaseEntity].
     * @return Uma instância de [DiseaseDao].
     */
    abstract fun diseaseDao(): DiseaseDao

    /**
     * Objeto companheiro para fornecer uma instância singleton do [AnimalDatabase].
     */
    companion object {
        /**
         * A instância volátil do [AnimalDatabase] para garantir que o acesso seja seguro entre threads.
         * `@Volatile` garante que as escritas nesta variável sejam imediatamente visíveis para outras threads.
         */
        @Volatile
        private var INSTANCE: AnimalDatabase? = null

        /**
         * Retorna a instância singleton do [AnimalDatabase].
         *
         * Se uma instância já existir, ela é retornada. Caso contrário, uma nova instância
         * é criada de forma segura para threads (thread-safe).
         * O banco de dados é criado a partir de um arquivo de asset "database/banco.db"
         * e utiliza fallback para migração destrutiva caso não haja um caminho de migração.
         *
         * @param context O contexto da aplicação, usado para obter o contexto da aplicação
         *                e para a construção do banco de dados.
         * @return A instância singleton de [AnimalDatabase].
         */
        fun getInstance(context: Context): AnimalDatabase {
            // Se a instância já existir, retorna ela.
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            // Bloco sincronizado para garantir que apenas uma thread crie a instância.
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Usa o contexto da aplicação para evitar memory leaks.
                    AnimalDatabase::class.java, // A classe do banco de dados.
                    "banco.db" // O nome do arquivo do banco de dados.
                )
                    .createFromAsset("database/banco.db") // Cria o banco de dados a partir de um arquivo pré-populado na pasta assets.
                    .fallbackToDestructiveMigration() // Se uma migração não for encontrada, o banco de dados será recriado (perdendo dados).
                    .build() // Constrói a instância do banco de dados.
                INSTANCE = instance // Atribui a nova instância à variável INSTANCE.
                return instance // Retorna a instância criada.
            }
        }
    }
}
