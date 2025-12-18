package com.nohana.projetoiot.model

import kotlin.collections.mutableListOf

/**
 * Representa um animal com seus dados e pontos de escuta associados.
 *
 * @property name O nome do animal.
 * @property imageUrl O ID do recurso da imagem para o animal.
 */
class Animal (
    var name: String,
    var imageUrl: Int
) {
    /** O identificador único do animal. */
    var id: Int = 0
    private var listeningPoints: MutableList<ListeningPoint> = mutableListOf()

    /**
     * Adiciona um novo ponto de escuta para este animal.
     * @param id O identificador do novo ponto de escuta.
     * @param position O nome ou identificador da posição do ponto de escuta.
     */
    fun newListeningPoint(id: Int, position : String) {
        val point = ListeningPoint(position)
        point.id = id
        listeningPoints.add(point)
    }

    /**
     * Recupera um ponto de escuta pela sua posição.
     * @param position O nome ou identificador da posição a ser buscada.
     * @return O [ListeningPoint] encontrado.
     */
    fun getListeningPointByPosition(position : String) : ListeningPoint {
        return listeningPoints.first { it.positionName == position }
    }

    /**
     * Adiciona uma nova doença a um ponto de escuta específico.
     * @param position A posição do ponto de escuta onde a doença será adicionada.
     * @param disease O nome da doença.
     * @param audio O identificador ou caminho do áudio associado à doença.
     * @param id O identificador opcional para a doença (padrão é 0).
     */
    fun addDiseaseToListeningPoint(position : String, disease : String, audio : String, id: Int = 0) {
        val listeningPoint = getListeningPointByPosition(position)
        listeningPoint.newDisease(disease, audio, id)
    }

    /**
     * Retorna a lista de todos os pontos de escuta associados a este animal.
     * @return Uma lista de [ListeningPoint].
     */
    fun getListeningPoints() : List<ListeningPoint> {
        return listeningPoints
    }

    /**
     * Define uma doença específica como ativa para um determinado ponto de escuta.
     * @param position A posição do ponto de escuta.
     * @param disease O nome da doença a ser definida como ativa.
     */
    fun setActiveDiseaseToListeningPoint(position : String, disease: String) {
        val listeningPoint = getListeningPointByPosition(position)
        val diseaseObject = listeningPoint.getDiseaseByName(disease)
        listeningPoint.selectedDisease(diseaseObject)
    }

    /**
     * Retorna uma representação em String do objeto [Animal].
     */
    override fun toString(): String {
        return "Animal(id=$id, name='$name', imageUrl=$imageUrl, listeningPoints=$listeningPoints)"
    }
}
