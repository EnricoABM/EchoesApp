package com.nohana.projetoiot.view.aparelhos

import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nohana.projetoiot.model.Device
import com.nohana.projetoiot.view.components.TitleHeader

@Composable
fun DevicesScreen() {
    TitleHeader("Aparelhos")

}

@Composable
fun DevicesGrid(devices: List<Device>) {

}

@Preview(showBackground = true)
@Composable
fun DevicesScreenPreview() {
    DevicesScreen()
}