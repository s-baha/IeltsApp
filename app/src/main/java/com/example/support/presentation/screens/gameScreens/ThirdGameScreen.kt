package com.example.support.presentation.screens.gameScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.support.domain.entity.ThirdGame
import com.example.support.presentation.screens.viewmodel.gameViewModels.ThirdGameViewModel
import com.example.support.presentation.ui.component.UserStatsPanel


@Composable
fun ThirdGameScreen(viewModel: ThirdGameViewModel = hiltViewModel()) {
    ThirdGameScreenContent(viewModel)
}

@Composable
fun ThirdGameScreenContent(viewModel: ThirdGameViewModel) {
    val user = viewModel.user.value?.username ?: "Unknown"
    val score = viewModel.score.value
    val snackBarHostState = remember { SnackbarHostState() }
    val sentence by viewModel.sentence.collectAsState()
    val context = LocalContext.current

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
                // user panel
                UserStatsPanel(user, score)
                // name of the game
                GameTexts("Choose Keywords")
                Spacer(modifier = Modifier.fillMaxHeight(0.02f))
                // box with the game
                ChooseKeywords(viewModel, sentence){viewModel.checkAnswer(context)}
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChooseKeywords(viewModel: ThirdGameViewModel, sentence: ThirdGame?, onClick: () -> Unit) {
    val selectedWords by viewModel.selectedWords.collectAsState()
    val isChecked = viewModel.isChecked.value
    val checkedWords by viewModel.checkedWords.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .fillMaxHeight(0.6f)
            .clip(RoundedCornerShape(27.dp))
            .background(Color(0xFF595D99)),
        contentAlignment = Alignment.Center
    ) {
        sentence?.let {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

                Spacer(modifier = Modifier.height(16.dp))

                FlowRow {
                    val wordsAndPhrases = extractWordsAndPhrases(it.text, it.answers)
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
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }



            }
        }
        Button(onClick =onClick){
            Text(text = "Check")
        }
    }
}

fun extractWordsAndPhrases(text: String, answers: List<String>): List<String> {
    val wordsAndPhrases = mutableListOf<String>()
    var remainingText = text

    // sort answers by descending length (so that we search for longer phrases first)
    val sortedAnswers = answers.sortedByDescending { it.length }

    while (remainingText.isNotEmpty()) {
        var found = false

        // for each answer, check if it is a prefix of the remaining text
        for (answer in sortedAnswers) {
            if (remainingText.startsWith(answer)) {
                wordsAndPhrases.add(answer) // add the answer to the list
                remainingText = remainingText.removePrefix(answer).trim() // remove the answer from the remaining text
                found = true
                break
            }
        }
        // if no answer was found, add the first word to the list
        if (!found) {
            val firstSpace = remainingText.indexOf(" ") // find the first space in the remaining text
            if (firstSpace != -1) {
                wordsAndPhrases.add(remainingText.substring(0, firstSpace)) // add the first word
                remainingText = remainingText.substring(firstSpace).trim() // remove the first word
            } else {
                wordsAndPhrases.add(remainingText) // add the remaining text
                break
            }
        }
    }

    return wordsAndPhrases
}
