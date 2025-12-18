package com.nohana.projetoiot.model

/**
 * Representa um ponto de escuta específico, que pode ter várias doenças associadas
 * e uma doença ativa selecionada.
 *
 * @property positionName O nome ou identificador da posição deste ponto de escuta.
 */
class ListeningPoint(
    var positionName: String
) {
    /** O identificador único do ponto de escuta. */
    var id: Int = 0
    private var activeDisease: Disease? = null
    private var diseases: MutableList<Disease> = mutableListOf()

    /**
     * Adiciona uma nova doença à lista de doenças associadas a este ponto de escuta.
     *
     * @param disease O nome da nova doença.
     * @param audioUrl A URL ou caminho para o arquivo de áudio da nova doença.
     * @param id O identificador único para a nova doença.
     */
    fun newDisease(disease : String, audioUrl: String, id: Int) {
        diseases.add(Disease(id = id, name = disease, audioUrl = audioUrl))
    }

    /**
     * Define uma doença existente como a doença ativa para este ponto de escuta.
     * A doença só é definida como ativa se já existir na lista de doenças deste ponto.
     *
     * @param disease O objeto [Disease] a ser definido como ativo.
     */
    fun selectedDisease(disease: Disease) {
        if (disease in diseases) {
            this.activeDisease = disease
        }
    }

    /**
     * Retorna a doença atualmente ativa neste ponto de escuta.
     *
     * @return O objeto [Disease] ativo, ou `null` se nenhuma doença estiver ativa.
     */
    fun getActiveDisease() : Disease? {
        return activeDisease
    }

    /**
     * Retorna uma lista imutável de todas as doenças associadas a este ponto de escuta.
     *
     * @return Uma [List] de objetos [Disease].
     */
    fun getDiseases() : List<Disease> {
        return diseases
    }

    /**
     * Busca e retorna uma doença pelo seu nome.
     *
     * @param disease O nome da doença a ser procurada.
     * @return O objeto [Disease] correspondente ao nome fornecido.
     * @throws NoSuchElementException se nenhuma doença com o nome especificado for encontrada.
     */
    fun getDiseaseByName(disease: String): Disease {
        return diseases.first {it.name == disease }
    }

    /**
     * Retorna uma representação em String do objeto [ListeningPoint],
     * incluindo seu nome de posição, ID, doença ativa e a lista de todas as doenças.
     */
    override fun toString(): String {
        return "ListeningPoint(positionName='$positionName', id=$id, activeDisease=$activeDisease, diseases=$diseases)"
    }
}
