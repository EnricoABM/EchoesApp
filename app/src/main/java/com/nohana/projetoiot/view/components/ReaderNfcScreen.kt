package com.nohana.projetoiot.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nohana.projetoiot.R
import com.nohana.projetoiot.view.ui.theme.ProjetoIotTheme
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderNfcScreen() {
    Column(
        modifier = Modifier
            .background(Color(0xFFC4DAEB))
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            title = { Text(
                text = "Leitor de Tag",
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

        Image(
            modifier = Modifier.width(250.dp),
            painter = painterResource(R.drawable.imagem_tela_leitor),
            contentDescription = "Imagem de Leitor de NFC"
        )

        Row() {
//            IconButton() { }
//            IconButton() { }
//            IconButton() { }

        }
        Row() {
//            IconButton() { }
//            IconButton() { }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun previewReaderNfcScreen() {
    ProjetoIotTheme() {
        ReaderNfcScreen()
    }
}