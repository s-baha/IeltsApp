package com.example.support.presentation.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.support.presentation.navigation.Screen
import com.example.support.presentation.screens.viewModels.MainMenuViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.support.R
import com.example.support.presentation.ui.component.UserStatsPanel


@Composable
fun SeeMoreScreen(
    viewModel: MainMenuViewModel = hiltViewModel(),
    onExitGame: () -> Unit,
    onNavigateTo: (String) -> Unit
) {
    val user = viewModel.user.value?.username.toString()
    val score = viewModel.score.value
    val rank = viewModel.rank.value

    val games = listOf(
        "Fact Or Opinions" to Screen.SecondGame.route,
        "Phrasal Verbs" to Screen.FirstGame.route,
        "Choose Keywords" to Screen.ThirdGame.route
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2E2B50))
    ) {
        UserStatsPanel(user, score,rank)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(games.chunked(2)) { rowGames ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowGames.forEach { (gameName, route) ->
                        GameCard(gameName,Modifier.weight(1f)) {
                            onNavigateTo(route)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun GameCard(gameName: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFF898FEC),
        modifier = modifier
            .padding(8.dp)
            .height(200.dp)
            .clickable { onClick() },
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = gameName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Image(
                painter = painterResource(id = R.drawable.undraw_team_up_qeem_1),
                contentDescription = null,
                modifier = Modifier.size(90.dp)
            )

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Text(text = "Play", color = Color.Black)
            }
        }
    }
}