package com.example.support.presentation.screens.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.support.R
import com.example.support.presentation.navigation.Screen
import com.example.support.presentation.viewModels.MainMenuViewModel

@Composable
fun GameCompleteScreen(
    viewModel: MainMenuViewModel,
    onNavigateTo: (String) -> Unit,
    onResetGame: () -> Unit,
    gameType: String
) {
    val scoreDifference by viewModel.scoreDifference.collectAsState()
    val rank = viewModel.rank.value


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2E2F50)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Game complete!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.undraw_team_up_qeem_1),
            contentDescription = "Game Complete",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Ranking Icon",
                    modifier = Modifier.size(40.dp)
                )
                Text(text = "Ranking", color = Color.White)
                Text(text = "$rank", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Points Icon",
                    modifier = Modifier.size(40.dp)
                )
                Text(text = "Points", color = Color.White)
                Text(text = "$scoreDifference", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопки
        Button(
            onClick = {
                onResetGame()
                onNavigateTo(
                    when (gameType) {
                        "first_game" -> Screen.FirstGame.route
                        "second_game" -> Screen.SecondGame.route
                        "third_game" -> Screen.ThirdGame.route
                        else -> Screen.FirstGame.route // По умолчанию
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Text(text = "New Game")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { onNavigateTo(Screen.MainMenu.route) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text(text = "Quit")
        }
    }
}

