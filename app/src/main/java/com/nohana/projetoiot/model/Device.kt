package com.nohana.projetoiot.model

/**
 * Representa um dispositivo no sistema.
 *
 * @property name O nome do dispositivo.
 * @property animal O tipo de [Animal] associado a este dispositivo (se houver).
 * @property isChecked Indica se o dispositivo está atualmente marcado ou selecionado.
 */
class Device(
    var name: String,
    var animal: Animal,
    var isChecked: Boolean
) {
    /**
     * Marca o dispositivo como selecionado (isChecked = true).
     * Se já estiver marcado, nenhuma alteração é feita.
     */
    fun check() {
        if (!isChecked) { // Verifica se não está checado antes de mudar
            isChecked = true
        }
    }

    /**
     * Desmarca o dispositivo (isChecked = false).
     * Se já estiver desmarcado, nenhuma alteração é feita.
     */
    fun uncheck() {
        if (isChecked) { // Verifica se está checado antes de mudar
            isChecked = false
        }
    }
}
