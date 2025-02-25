@file:Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")

package com.example.support.presentation.screens.gameScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.support.presentation.navigation.Screen
import com.example.support.presentation.screens.viewmodel.gameViewModels.SecondGameViewModel
import com.example.support.presentation.ui.component.UserStatsPanel


@Composable
fun SecondGameScreen(
    viewModel: SecondGameViewModel = hiltViewModel(),
    onNavigateTo: (Screen) ->Unit,
    onExitGame: () -> Unit
) {
    SecondGameScreenContent(viewModel)
}

@Composable
fun SecondGameScreenContent(viewModel: SecondGameViewModel) {

    val question = viewModel.currentQuestion.value
    val user = viewModel.user.value?.username ?: "Unknown"
    val score = viewModel.score.value
    val rank = viewModel.rank.value
    val errorMessage = viewModel.errorMessage.value
    val snackBarHostState = remember { SnackbarHostState() }


    // show error message
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
                //user panel
                UserStatsPanel(user, score , rank)
                //
                GameTexts("Fact or Opinion")
                Spacer(modifier = Modifier.fillMaxHeight(0.02f))

                question?.let {
                    QuestionCard(viewModel,it.text)
                }


            }
        }
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
        Column(verticalArrangement = Arrangement.SpaceBetween ) {
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
        modifier = Modifier
        .padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE6D8F8),
            contentColor = Color.Black
        )) {
        Text(
            text = answerText,
        )
    }
}


@Composable
fun GameTexts(text: String){
    Column(modifier = Modifier.padding(5.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        Text(text, fontSize = 32.sp, fontWeight = FontWeight.W600,color = Color(0xE6E6D8F8))
    }
}