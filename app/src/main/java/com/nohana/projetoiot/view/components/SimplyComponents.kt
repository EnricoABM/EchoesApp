package com.nohana.projetoiot.view.components

import android.app.Activity
import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nohana.projetoiot.R
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

@Composable
fun TitleHeaderWithBackArrow(
    title: String,
    activity: Activity
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = { activity.finish() }
        ) {
            Icon(painter = painterResource( R.drawable.back_arrow), contentDescription = "Back button")
        }
        Text(
            fontSize = 36.sp,
            text = title,
            modifier = Modifier.weight(1f)
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