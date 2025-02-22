package com.example.support.presentation.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.support.R

@Composable
fun MenuTop() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .clip(shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        Color(-0x767014),  // 0%
                        Color(-0x767014),  // 58%
                        Color(-0xa6a267) // 100%
                    )
                )),
        contentAlignment = Alignment.Center
    ){
        Text(
            text=( stringResource(id= R.string.app_name) ),
            color = Color.White,
            fontSize = 48.sp,
            fontWeight = FontWeight.W600,
        )
    }
}