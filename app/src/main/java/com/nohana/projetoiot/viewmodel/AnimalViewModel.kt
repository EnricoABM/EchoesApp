package com.nohana.projetoiot.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nohana.projetoiot.controller.animal.AnimalController
import com.nohana.projetoiot.model.animal.Animal
import com.nohana.projetoiot.model.animal.Disease
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnimalViewModel(
    context: Context
): ViewModel() {

    private val controller: AnimalController = AnimalController(context)

    val animals: StateFlow<List<Animal>> = controller
        .getAnimals()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _selectedAnimal = MutableStateFlow<Animal?>(null)
    val selectedAnimal: StateFlow<Animal?> = _selectedAnimal.asStateFlow()

    fun selectAnimal(animal: Animal) {
        _selectedAnimal.value = animal
    }

    fun updateActiveDisease(pointId: Int, newDisease: Disease) {
        val currentAnimal = _selectedAnimal.value ?: return

        // Criamos uma nova lista de pontos preservando os IDs,
        // alterando apenas a activeDisease do ponto clicado.
        val updatedPoints = currentAnimal.listeningPoints.map { point ->
            if (point.id == pointId) {
                point.copy(activeDisease = newDisease)
            } else {
                point
            }
        }

        // Atualizamos o estado com a CÓPIA do animal (o ID original permanece o mesmo)
        _selectedAnimal.value = currentAnimal.copy(listeningPoints = updatedPoints)
    }

    fun saveToDatabase() {
        _selectedAnimal.value?.let { animal ->
            viewModelScope.launch {
                controller.updateActiveDiseases(animal)
                // Opcional: Limpar seleção ou mostrar sucesso
            }
        }
    }
}