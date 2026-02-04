package com.nohana.projetoiot.model.animal


data class ListeningPoint(
    var id: Int = 0,
    var positionName: String,
    var activeDisease: Disease? = null,
    var diseases: List<Disease> = emptyList()
) {

}
