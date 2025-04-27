package com.example.support.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.support.domain.entity.FourthGame

@Composable
fun HeaderSection(selectedWordData: FourthGame) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Category: ${selectedWordData.category}", color = Color.White, fontSize = 20.sp)
        Text(text = "Main Word: ${selectedWordData.mainWord}", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}
