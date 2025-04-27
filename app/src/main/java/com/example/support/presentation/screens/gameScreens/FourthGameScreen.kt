package com.example.support.presentation.screens.gameScreens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.support.presentation.ui.component.GameOverDialog
import com.example.support.presentation.ui.component.HeaderSection
import com.example.support.presentation.ui.component.PauseOverlay
import com.example.support.presentation.ui.component.PointsSection
import com.example.support.presentation.ui.component.TimerDisplaySection
import com.example.support.presentation.viewModels.gameViewModels.FourthGameViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.abs
import kotlin.random.Random
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.draw.alpha
import kotlinx.coroutines.delay
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.unit.Dp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FourthGameScreen(
    viewModel: FourthGameViewModel = hiltViewModel(),
    onNavigateTo: (String) -> Unit,
    onExitGame: () -> Unit,
) {

    val score by viewModel.score
    val wrongCatches by viewModel.wrongCatches
    val timeLeft by viewModel.timeLeft
    val isPaused by viewModel.isPaused
    val isGameOver by viewModel.isGameOver
    val selectedWordData by viewModel.selectedWordData
    val wordsList by viewModel.wordsList
    val restartKey by viewModel.restartKey

    val context = LocalContext.current
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    var isTimeUp by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        if (!isGameOver) {
            viewModel.togglePause()
        } else {
            onExitGame()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF4B4E78))
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    if (dragAmount > 50f) {
                        if (!isGameOver) {
                            viewModel.togglePause()
                        } else {
                            onExitGame()
                        }
                        change.consume()
                    }
                }
            }
    ) {
        // Игровая зона
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = if (isPaused) 0.2f else 1f
                }
        ) {
            if (isPaused) {
                PauseOverlay(onExitGame,onResume = { viewModel.togglePause() })
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 370.dp)
            ) {
                key(restartKey) {
                    FallingTexts(
                        onCatch = { word, isCorrect ->
                            if (!isCorrect) {
                                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                            }
                            viewModel.catchWord(word)
                        },
                        offsetX = 0f,
                        wordsList = wordsList,
                        isPaused = isPaused
                    )
                }
            }

            HeaderSection(selectedWordData = selectedWordData)
            PointsSection(score = score)
            TimerDisplaySection(timeLeft = timeLeft, onTimeUp = {
                isTimeUp = true
                viewModel.catchWord("") // чтобы остановить игру
            })
        }

        // Если время вышло или проигрыш
        if (isTimeUp || isGameOver) {
            GameOverDialog(
                onRestart = {
                    viewModel.restartGame()
                    isTimeUp = false
                }
            )
        }
    }
}

@Composable
fun FallingTexts(
    onCatch: (String, Boolean) -> Unit,
    offsetX: Float,
    wordsList: List<String>,
    isPaused: Boolean
) {
    val screenWidthPx = with(LocalDensity.current) { 360.dp.toPx() }
    val columnWidthPx = screenWidthPx / 3
    var offsetX1 by remember { mutableStateOf(0f) }

    val fallingWordsState = remember { mutableStateMapOf<String, FallingWord>() }

    val textWidths = wordsList.associateWith { text ->
        with(LocalDensity.current) { (text.length * 7f).dp.toPx() }
    }

    LaunchedEffect(wordsList) {
        val currentWords = fallingWordsState.keys.toSet()
        val newWords = wordsList.toSet()

        currentWords.filter { it !in newWords }.forEach { fallingWordsState.remove(it) }

        val numColumns = 3
        newWords.filter { it !in currentWords }.forEachIndexed { index, text ->
            if (fallingWordsState[text] == null) {
                val columnIndex = index % numColumns
                val occupiedXs = fallingWordsState.values.map { it.initialX }
                var xPosition: Float
                var attempts = 0

                val approximateTextWidthPx = textWidths[text] ?: 0f

                do {
                    val columnStart = columnIndex * columnWidthPx
                    val maxColumnX = columnWidthPx - approximateTextWidthPx - 30f
                    val maxX = (columnStart + maxColumnX).coerceAtMost(screenWidthPx - approximateTextWidthPx)
                    val minX = columnStart + 30f
                    xPosition = Random.nextFloat() * (maxX - minX) + minX
                    attempts++
                    if (attempts > 10) break
                } while (occupiedXs.any { abs(it - xPosition) < 30 })

                xPosition = xPosition.coerceIn(30f, screenWidthPx - approximateTextWidthPx)
                fallingWordsState[text] = FallingWord(text, xPosition)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        fallingWordsState.values.forEachIndexed { index, fallingWord ->
            key(fallingWord.text) {
                FallingText(
                    text = fallingWord.text,
                    columnStart = 0,
                    columnWidth = columnWidthPx.toInt(),
                    onCatch = onCatch,
                    offsetX = offsetX1,
                    initialX = fallingWord.initialX,
                    delayIndex = index,
                    isPaused = isPaused
                )
            }
        }
        MovingBoard(
            boxHeight = 461.dp,
            boxWidth = 90.dp,
            onCatch = onCatch,
            onOffsetChanged = { offset -> offsetX1 = offset }
        )
    }
}

data class FallingWord(
    val text: String,
    val initialX: Float
)
@Composable
fun FallingText(
    text: String,
    columnStart: Int,
    columnWidth: Int,
    onCatch: (String, Boolean) -> Unit,
    offsetX: Float,
    initialX: Float,
    delayIndex: Int,
    isPaused: Boolean
) {
    val density = LocalDensity.current
    val screenWidthPx = with(density) { 360.dp.toPx() }
    val approximateTextWidth = with(density) { (text.length * 7f).dp.toPx() }
    val xPositionPx = initialX.coerceAtMost(screenWidthPx - approximateTextWidth)

    var isCaught by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }
    var isAnimating by remember { mutableStateOf(false) }

    val yPosition = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }
    val fontSize = remember { Animatable(20f) }

    LaunchedEffect(Unit, isPaused) {
        delay(500L + delayIndex * 200L)
        isVisible = true
        if (!isPaused) {
            yPosition.animateTo(
                targetValue = 900f,
                animationSpec = tween(durationMillis = 8000, easing = LinearEasing)
            )
        }
    }

    LaunchedEffect(yPosition.value, isPaused) {
        if (!isPaused && !isCaught) {
            val boardTop = 461f - 25f
            val catchZoneTop = boardTop - 30f
            val catchZoneBottom = boardTop

            if (yPosition.value in catchZoneTop..catchZoneBottom) {
                val boardLeft = offsetX
                val boardRight = offsetX + with(density) { 90.dp.toPx() }
                val wordLeft = xPositionPx
                val wordRight = xPositionPx + approximateTextWidth
                if (wordRight >= boardLeft && wordLeft <= boardRight) {
                    isCaught = true
                    isCorrect = true // Можно сделать свою проверку
                    isAnimating = true
                    yPosition.stop()
                    onCatch(text, isCorrect)
                    alpha.animateTo(0f, tween(500))
                    fontSize.animateTo(30f, tween(500))
                    delay(500)
                    isAnimating = false
                    isVisible = false
                }
            } else if (yPosition.value > 900f && !isCaught) {
                isCaught = true
                isCorrect = false
                onCatch(text, false)
                isVisible = false
            }
        }
    }

    if (isVisible || isAnimating) {

        Text(
            text = text,
            modifier = Modifier
                .alpha(alpha.value)
                .layoutId(text)
                .absoluteOffset(x = with(density) { xPositionPx.toDp() }, y = yPosition.value.dp),
            fontSize = fontSize.value.sp,
            color = Color.White
        )
    }
}


@Composable
fun MovingBoard(
    boxHeight: Dp,
    boxWidth: Dp,
    onCatch: (String, Boolean) -> Unit,
    onOffsetChanged: (Float) -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val density = LocalDensity.current
    val screenWidthPx = with(density) { 430.dp.toPx() }
    val boardWidthPx = with(density) { boxWidth.toPx() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetX = offsetX.coerceIn(0f, screenWidthPx - boardWidthPx)
                    onOffsetChanged(offsetX)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .absoluteOffset(x = with(density) { offsetX.toDp() }, y = boxHeight - 25.dp)
                .height(25.dp)
                .width(boxWidth)
                .background(Color.Gray, RoundedCornerShape(15.dp))
        )
    }
}