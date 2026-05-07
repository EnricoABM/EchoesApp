package com.nohana.projetoiot.model.animal


data class ListeningPoint(
    var id: Int = 0,
    var positionName: String,
    var activeScenario: Scenario? = null,
    var scenarios: List<Scenario> = emptyList()
) {

}
