package com.nohana.projetoiot.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa a entidade 'ListeningPoint' na tabela do banco de dados Room.
 *
 * Esta data class define a estrutura da tabela de pontos de escuta, onde cada instância
 * representa uma linha na tabela. Cada ponto de escuta está associado a um [AnimalEntity]
 * através do [animalId] e pode ter uma doença ativa identificada por [activeDiseaseId].
 *
 * @property id O identificador único para o ponto de escuta. É a chave primária e é gerada automaticamente.
 * @property position O nome ou identificador da posição deste ponto de escuta.
 * @property animalId O ID do [AnimalEntity] ao qual este ponto de escuta está associado.
 *                    Serve como uma chave estrangeira para a tabela de animais.
 * @property activeDiseaseId O ID da [DiseaseEntity] que está atualmente ativa para este ponto de escuta.
 *                           Pode ser nulo se nenhuma doença estiver ativa. Serve como uma chave
 *                           estrangeira opcional para a tabela de doenças.
 */
@Entity // Marca esta classe como uma tabela no banco de dados Room.
data class ListeningPointEntity(
    /**
     * Chave primária da tabela.
     */
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0, // O valor padrão 0 é usado antes da inserção, quando o ID ainda não foi gerado.

    /**
     * O nome ou identificador da posição deste ponto de escuta.
     */
    var position: String, // Renomeado de positionName para position para corresponder ao código fornecido.

    /**
     * O identificador do animal ([AnimalEntity]) ao qual este ponto de escuta pertence.
     * Este campo será uma coluna na tabela e normalmente seria usado para estabelecer
     * uma relação de chave estrangeira com a tabela de animais.
     */
    var animalId: Int,

    /**
     * O identificador da doença ([DiseaseEntity]) atualmente ativa para este ponto de escuta, se houver.
     */
    var activeDiseaseId: Int? = null // Permite que o campo seja nulo, indicando nenhuma doença ativa.
) {
}

