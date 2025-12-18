package com.nohana.projetoiot.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa a entidade 'Disease' na tabela do banco de dados.
 *
 * @property id O identificador único para a doença.
 * @property name O nome da doença.
 * @property audioUrl A URL ou caminho para o arquivo de áudio associado a esta doença.
 * @property listeningPointId O ID do [ListeningPointEntity] ao qual esta doença está associada.
 *                            Serve como uma chave estrangeira para a tabela de pontos de escuta.
 */
@Entity // Marca esta classe como uma tabela no banco de dados.
data class DiseaseEntity(
    /**
     * Chave primária da tabela.
     * quando uma nova DiseaseEntity é inserida.
     */
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0, // O valor padrão 0 é usado antes da inserção, quando o ID ainda não foi gerado.

    /**
     * O nome da doença.
     */
    var name: String,

    /**
     * A URL ou o caminho para o arquivo de áudio relacionado à doença.
     */
    var audioUrl: String,

    /**
     * O identificador do ponto de escuta ([ListeningPointEntity]) ao qual esta doença pertence.
     */
    var listeningPointId: Int
) {
    // O corpo da classe está vazio, pois 'data class' com propriedades no construtor primário
    // já define os campos. Funções como equals(), hashCode(), toString() são geradas automaticamente.
}
