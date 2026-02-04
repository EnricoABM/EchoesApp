package com.nohana.projetoiot.model.animal

data class Animal (
    var id: Int = 0,
    var name: String,
    var imageUrl: Int, // Id do recurso
    var listeningPoints: List<ListeningPoint> = emptyList()
) {}
