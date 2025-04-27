package com.example.support.presentation.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GameOverDialog(onRestart: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            Button(onClick = onRestart) { Text("Restart") }
        },
        title = { Text("Game Over") },
        text = { Text("You caught 3 wrong words or time is up. Restart?") }
    )
}
