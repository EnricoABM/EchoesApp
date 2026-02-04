package com.nohana.projetoiot.view.nfc

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nohana.projetoiot.view.components.AnimalConfigScreen
import com.nohana.projetoiot.view.ui.theme.ProjetoIotTheme
import com.nohana.projetoiot.viewmodel.AnimalViewModel

class MainNfcActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animalViewModel = AnimalViewModel(this)

        setContent {
            ProjetoIotTheme {
                AnimalConfigScreen(
                    viewModel = animalViewModel,
                    onConfirm = { it ->
                        animalViewModel.saveToDatabase()
                    }
                )
            }
        }
    }
}
