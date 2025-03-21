@file:Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")

package com.example.support.presentation.screens.gameScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.support.presentation.navigation.Screen
import com.example.support.presentation.screens.viewModels.gameViewModels.SecondGameViewModel
import com.example.support.presentation.ui.component.UserStatsPanel
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun SecondGameScreen(
    viewModel: SecondGameViewModel = hiltViewModel(),
    onNavigateTo: (String) -> Unit,
    onExitGame: () -> Unit
) {
    SecondGameScreenContent(viewModel, onExitGame,onNavigateTo)
}
@Composable
fun SecondGameScreenContent(viewModel: SecondGameViewModel, onExitGame: () -> Unit,onNavigateTo: (String) -> Unit) {
    val question = viewModel.currentQuestion.value
    val user = viewModel.user.value?.username ?: "Unknown"
    val score = viewModel.score.value
    val rank = viewModel.rank.value
    val errorMessage = viewModel.errorMessage.value
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val timeLeft by viewModel.timeLeft.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = viewModel) {
        viewModel.startTimer{ showDialog=true }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackBarHostState.showSnackbar(it)
            viewModel.clearErrorMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF4B4E78))
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFF4B4E78))
                    .padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                UserStatsPanel(user, score, rank)
                GameTexts("Fact or Opinion")
                Spacer(modifier = Modifier.fillMaxHeight(0.02f))
                CircularTimer(timeLeft)
                Spacer(modifier = Modifier.fillMaxHeight(0.02f))

                question?.text?.let {
                    QuestionCard(viewModel, it)
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onNavigateTo(Screen.GameComplete.route) },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF595D99),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Pause")
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Time's Up!") },
            text = { Text("The game is over. Do you want to exit and restart?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onExitGame()
                    }
                ) {
                    Text("Exit and Restart")
                }
            }
        )
    }
}

@Composable
fun CircularTimer(timeLeft: Int) {
    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(40.dp)) {
            drawArc(
                color = Color.Gray,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(4.dp.toPx())
            )
            drawArc(
                color = Color.Green,
                startAngle = -90f,
                sweepAngle = (timeLeft / 30f).coerceIn(0f, 1f) * 360f,
                useCenter = false,
                style = Stroke(4.dp.toPx())
            )
        }
        Text(text = "$timeLeft", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun QuestionCard(
    viewModel: SecondGameViewModel,
    questionText: String
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .fillMaxHeight(0.6f)
            .clip(shape = RoundedCornerShape(27.dp))
            .background(Color(0xFF595D99)),
        contentAlignment = Alignment.Center
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = questionText,
                modifier = Modifier.padding(horizontal = 55.dp, vertical = 15.dp),
                color = Color(0xFFFFFDFF),
                fontSize = 24.sp,
                fontWeight = FontWeight.W400
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FactOrOpinionButton("Fact") { viewModel.checkAnswer("Fact", context) }
                FactOrOpinionButton("Opinion") { viewModel.checkAnswer("Opinion", context) }
            }
        }
    }
}

@Composable
fun FactOrOpinionButton(answerText: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE6D8F8),
            contentColor = Color.Black
        )
    ) {
        Text(text = answerText)
    }
}

@Composable
fun GameTexts(text: String) {
    Column(
        modifier = Modifier.padding(5.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text, fontSize = 32.sp, fontWeight = FontWeight.W600, color = Color(0xE6E6D8F8))
    }
}
