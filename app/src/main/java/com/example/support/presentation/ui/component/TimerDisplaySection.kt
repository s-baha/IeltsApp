package com.example.support.presentation.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TimerDisplaySection(timeLeft: Int, onTimeUp: () -> Unit) {
    TimerDisplay(timeLeft = timeLeft, onTimeUp = onTimeUp, resetGameState = true)
}
@Composable
fun TimerDisplay(timeLeft: Int, onTimeUp: () -> Unit, resetGameState: Boolean) {
    var timerValue by remember { mutableStateOf(timeLeft) }
    var prevTimeLeft by remember { mutableStateOf(timeLeft) }

    LaunchedEffect(timeLeft, resetGameState) {
        if (resetGameState) {
            timerValue = timeLeft
            prevTimeLeft = timeLeft
        }
        if (timerValue > 0) {
            delay(1000L)
            prevTimeLeft = timerValue
            timerValue--
        } else {
            onTimeUp()
        }
    }

    val animatedSize by animateFloatAsState(
        targetValue = if (timeLeft != prevTimeLeft) 2f else 1f,
        animationSpec = tween(durationMillis = 800)
    )

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFF34C759)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$timerValue",
            fontSize = (20 * animatedSize).sp,
            color = Color.White
        )
    }
}

