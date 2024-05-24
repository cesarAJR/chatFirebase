package com.example.prueba_softtek.component

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.android.material.progressindicator.CircularProgressIndicator

@Composable
fun DialogLoading(visible:Boolean) {

    if (visible){
        Dialog(onDismissRequest = {

        }) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = Color.Cyan,
            )
        }
    }

}