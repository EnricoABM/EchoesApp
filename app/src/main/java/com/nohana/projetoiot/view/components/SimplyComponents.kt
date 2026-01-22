package com.nohana.projetoiot.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nohana.projetoiot.view.ui.theme.ProjetoIotTheme

@Composable
fun TitleHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            fontSize = 36.sp,
            text = title
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewTitle() {
    ProjetoIotTheme() {
        TitleHeader("Header")
    }
}