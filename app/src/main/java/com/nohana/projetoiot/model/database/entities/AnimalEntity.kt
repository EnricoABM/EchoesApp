package com.nohana.projetoiot.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa a entidade 'Animal' na tabela do banco de dados Room.
 *
 * @property id O identificador único para o animal. É a chave primária e é gerada automaticamente.
 * @property name O nome do animal.
 * @property imageUrl O ID do recurso drawable para a imagem do animal.
 */
@Entity // Marca esta classe como uma tabela no banco de dados.
data class AnimalEntity(
    /**
     * Chave primária da tabela.
     */
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0, // O valor padrão 0 é usado antes da inserção, quando o ID ainda não foi gerado.

    /**
     * O nome do animal.
     */
    var name: String,

    /**
     * O identificador do recurso da imagem para o animal.
     */
    var imageUrl: Int
) {}
