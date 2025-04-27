package com.example.support.presentation.screens.gameScreens

import android.os.CountDownTimer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.support.domain.entity.ThirdGame
import com.example.support.presentation.navigation.Screen
import com.example.support.presentation.viewModels.gameViewModels.ThirdGameViewModel
import com.example.support.presentation.ui.component.UserStatsPanel
// SELECT KEYWORDS
@Composable
fun ThirdGameScreen(
    viewModel: ThirdGameViewModel = hiltViewModel(),
    onNavigateTo: (String) ->Unit,
    onExitGame: () -> Unit)
{
    ThirdGameScreenContent(viewModel,onNavigateTo,onResetGame = { viewModel.resetGame() })
}

@Composable
fun ThirdGameScreenContent(viewModel: ThirdGameViewModel, onNavigateTo: (String) -> Unit, onResetGame: () -> Unit) {
    val user = viewModel.user.value?.username ?: "Unknown"
    val score = viewModel.score.value
    val rank = viewModel.rank.value
    val snackBarHostState = remember { SnackbarHostState() }
    val sentence by viewModel.sentence.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startTimer {
            onResetGame()
            onNavigateTo("${Screen.GameComplete.route}/third_game") // передай правильный gameType
        }
    }


    LaunchedEffect(viewModel.errorMessage.value) {
        viewModel.errorMessage.value?.let {
            snackBarHostState.showSnackbar(it)
            viewModel.clearErrorMessage() // clear error message after showing
        }
    }
    LaunchedEffect(Unit) {
        viewModel.loadRandomSentence() // load a random sentence when the screen is opened
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF4B4E78))
    ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFF4B4E78))
                    .padding(),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                // user panel
                UserStatsPanel(user, score, rank)
                // name of the game
                GameTexts("Choose Keywords")
                // space between name and timer
                Spacer(modifier = Modifier.fillMaxHeight(0.02f))
                CircularTimer(timeLeft)
                Spacer(modifier = Modifier.fillMaxHeight(0.02f))
                // box with the game
                ChooseKeywords(viewModel, sentence,onNavigateTo)
            }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChooseKeywords(viewModel: ThirdGameViewModel, sentence: ThirdGame?, onNavigateTo: (String) -> Unit) {
    val selectedWords by viewModel.selectedWords.collectAsState()
    val isChecked = viewModel.isChecked.value
    val checkedWords by viewModel.checkedWords.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .fillMaxHeight(0.6f)
            .clip(RoundedCornerShape(27.dp))
            .background(Color(0xFF595D99)),
        contentAlignment = Alignment.Center
    ) {
        sentence?.let {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp, horizontal = 30.dp)
            ) {


                FlowRow {
                    val wordsAndPhrases = viewModel.extractWordsAndPhrases(it.text, it.answers)
                    wordsAndPhrases.forEach { phrase ->
                        val isSelected = selectedWords.contains(phrase)

                        Text(
                            text = phrase,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    when {
                                        // check if the word is correct
                                        checkedWords?.containsKey(phrase) == true -> {
                                            if (checkedWords?.get(phrase) == true) Color.Green else Color.Red
                                        }
                                        // check if the word is selected
                                        !isChecked && isSelected -> Color.LightGray
                                        else -> Color(0xFF595D99)
                                    }
                                )
                                .clickable { viewModel.selectWord(phrase) }
                                .padding(vertical = 8.dp, horizontal = 3.dp),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

            }
        }
    }
    Row (
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {

        Button(
            onClick = { onNavigateTo(Screen.MainMenu.route)},
            modifier = Modifier
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF595D99),
                contentColor = Color.White
            )){
            Text(text = "Pause")
        }
        Button(
            onClick = {
                viewModel.checkAnswer(context = context)
            },
            modifier = Modifier
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF595D99),
                contentColor = Color.White
            )){
            Text(text = "Check")
        }
    }
}