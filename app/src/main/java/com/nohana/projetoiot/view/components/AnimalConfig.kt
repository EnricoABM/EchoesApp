package com.nohana.projetoiot.view.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nohana.projetoiot.R
import com.nohana.projetoiot.model.animal.Animal
import com.nohana.projetoiot.model.animal.Scenario
import com.nohana.projetoiot.viewmodel.AnimalViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalConfigScreen(
    viewModel: AnimalViewModel,
    onConfirm: (Animal?) -> Unit
) {
    val animals by viewModel.animals.collectAsState()
    val selectedAnimal by viewModel.selectedAnimal.collectAsState()

    Column(
        modifier = Modifier
            .background(Color(0xFFC4DAEB))
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CenterAlignedTopAppBar(
            title = { Text(
                text = "Configuração",
                fontSize = 36.sp
            )},
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFFC4DAEB), // Mesma cor do fundo
                titleContentColor = Color.Black,    // Cor do texto
                navigationIconContentColor = Color.Black // Cor do ícone
            ),
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(R.drawable.back_arrow),
                        contentDescription = "Voltar"
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .width(305.dp)
                .height(260.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
                .background(Color.White, RoundedCornerShape(15.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Dropdown de seleção de Animal
            AnimalSelector(
                animals = animals,
                selectedAnimal = selectedAnimal,
                onAnimalSelected = { viewModel.selectAnimal(it) }
            )

            HorizontalDivider()

            Log.d("IMG", R.drawable.cachorro.toString())

            Image(
                painter = painterResource(R.drawable.animal_placeholder),
                contentDescription = "Imagem do Animal",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                    .fillMaxSize()
            )

        }

        // 2. Se houver animal, mostra os pontos
        selectedAnimal?.let { animal ->
            LazyColumn(
                modifier = Modifier
                    .width(305.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
                    .background(Color.White, RoundedCornerShape(15.dp))
                    .padding(18.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(animal.listeningPoints) { point ->
                    DiseaseSelector(
                        pointName = point.positionName,
                        scenarios = point.scenarios,
                        activeScenario = point.activeScenario,
                        onScenarioSelected = { newScenario ->
                            viewModel.updateActiveScenario(point.id, newScenario)
                        }
                    )
                }
            }
        }

    Button(
        onClick = { selectedAnimal.let { onConfirm(it) } },
        enabled = selectedAnimal != null,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) { Text("Confirmar e Enviar") }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalSelector(
    animals: List<Animal>,
    selectedAnimal: Animal?,
    onAnimalSelected: (Animal) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // O "Box" que gerencia o estado do menu
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        TextField(
            value = selectedAnimal?.name ?: "Selecione um Animal",
            onValueChange = {},
            readOnly = true,
//            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            animals.forEach { animal ->
                DropdownMenuItem(
                    text = { Text(animal.name) },
                    onClick = {
                        onAnimalSelected(animal)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiseaseSelector(
    pointName: String,
    scenarios: List<Scenario>,
    activeScenario: Scenario?,
    onScenarioSelected: (Scenario) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
//        Text(text = pointName, modifier = Modifier.weight(1f))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.weight(1.5f)
        ) {
            OutlinedTextField(
                value = activeScenario?.name ?: "Nenhuma",
                onValueChange = {},
                readOnly = true,
                label = { Text(pointName)},
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                scenarios.forEach { scenario ->
                    DropdownMenuItem(
                        text = { Text(scenario.name) },
                        onClick = {
                            onScenarioSelected(scenario)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
