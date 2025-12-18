package com.nohana.projetoiot.model

/**
 * Representa uma doença que pode ser associada a um ponto de escuta de um animal.
 *
 * @property id O identificador único da doença. Valor padrão é 0.
 * @property name O nome da doença.
 * @property audioUrl A URL ou caminho para o arquivo de áudio associado a esta doença.
 */
data class Disease(
    var id: Int = 0,
    var name : String,
    var audioUrl : String
) {
}