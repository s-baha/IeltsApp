package com.example.support.presentation.screens.gameScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.support.presentation.navigation.Screen
import com.example.support.presentation.screens.viewmodel.gameViewModels.FirstGameViewModel
import com.example.support.presentation.ui.component.UserStatsPanel

@Composable
fun FirstGameScreen(
    viewModel: FirstGameViewModel = hiltViewModel(),
    onNavigateTo: (Screen) ->Unit,
    onExitGame: () -> Unit
){
    LaunchedEffect(Unit) {
        viewModel.loadUser()
        viewModel.loadNewQuestion()
    }
    FillInTheBlankPage(onExitGame=onExitGame)
}

@Composable
fun FillInTheBlankPage(viewModel: FirstGameViewModel = hiltViewModel(), onExitGame: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val question = viewModel.currentQuestion.value
    val userAnswer = viewModel.userAnswer.value
    val user = viewModel.user.value?.username.toString()
    val rank = viewModel.rank.value
    val score = viewModel.score.value
    val errorMessage = viewModel.errorMessage.value
    val snackBarHostState = remember { SnackbarHostState() }

    //for error message when entered incorrect answer
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackBarHostState.showSnackbar(it)
            viewModel.clearErrorMessage()
        }
    }


        // hide keyboard when touch
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            keyboardController?.hide()
                        }
                    )
                }
        ) {
            //shows snack bar with message
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
            ) {contentPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFF4B4E78))
                        .padding(contentPadding)
                )
                {
                    // user stats
                    UserStatsPanel(user, score,rank)
                    BackHandler {
                        onExitGame() // Вернуться в MainMenuScreen
                    }
                    Spacer(modifier = Modifier.fillMaxHeight(0.02f))
                    // name of game
                    GameTexts("Phrasal verbs match up")
                    // if question not null, show question
                    question?.let { it ->
                        FillInTheBlank(
                            text1 = it.text1,
                            text2 = it.text2,
                            userAnswer = userAnswer,
                            onAnswerChange = {
                                viewModel.onAnswerChange(it)
                            }
                        )
                    }
            }
        }
    }
}

// TextField for answer
@Composable
fun FillInTheBlank(
    text1 : String,
    text2 : String,
    userAnswer : String,
    onAnswerChange : (String) ->Unit,
    viewModel: FirstGameViewModel = hiltViewModel()
){
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.6f)
            .padding(20.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = Color(0xFF595D99))
    ) {
        AnswerBlank(text1,text2,userAnswer,onAnswerChange)
        SubmitButton { viewModel.checkAnswer(context) }
    }
}

// Button to submit
@Composable
fun SubmitButton(onClick: () -> Unit){

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxWidth()

    ){
        Button(onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE6D8F8),
                contentColor = Color.Black
            ) ) {
            Text("Submit")
        }
    }
}

// Box with text
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnswerBlank(
    text1: String,
    text2: String,
    userAnswer: String,
    onValueChange: (String) -> Unit
){
    FlowRow (modifier = Modifier.padding(horizontal = 25.dp, vertical = 15.dp)){
        Text(
            text1,
            fontSize = 24.sp,
            fontWeight = FontWeight.W400,
            color = Color(0xFFFFFDFF)
        )
        BasicTextField(
            userAnswer,
            textStyle = TextStyle(fontSize = 24.sp),
            onValueChange = onValueChange,
            modifier = Modifier

                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = Color(0xFFE6D8F8))
        )
        Text(
            text2,
            fontSize = 24.sp,
            fontWeight = FontWeight.W400,
            color = Color(0xFFFFFDFF)

        )
    }

}