package com.example.support.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.support.presentation.ui.component.ProfileBox
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.support.R
import com.example.support.presentation.navigation.Screen
import com.example.support.presentation.screens.viewmodel.AuthViewModel
import com.example.support.presentation.screens.viewmodel.MainMenuViewModel
import com.example.support.presentation.ui.component.RowGames

@Composable
fun MainMenuScreen(
    onNavigateTo: (String) -> Unit,
) {
        MainPage(onNavigateTo = onNavigateTo)
}

@Composable
fun MainPage(
    viewModel: MainMenuViewModel = hiltViewModel(),
    onNavigateTo: (String) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
){
    val user = viewModel.user.value?.username.toString()
    val score = viewModel.score.value


    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize().padding()
            .background(color = Color(0xFF4B4E78)),
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(
                bottomStart = 24.dp,
                bottomEnd = 24.dp)
            )

        ) {
            Column(
            ) {
                ProfileBox(user, score)
                RowGames()
                LazyRow(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val games = listOf(
                        "Fact Or Opinions" to Screen.SecondGame.route,
                        "Phrasal Verbs" to Screen.FirstGame.route
                    )

                    items(games) { (gameName, route) ->
                        GamesCard(gameName) {
                            onNavigateTo(route)
                        }
                    }
                }
                Button(onClick = {
                    authViewModel.logout()
                    onNavigateTo(Screen.Login.route)
                }) {
                    Text("Log out")
                }

            }
        }
    }
}

// Games Card
@Composable
fun GamesCard(gameText: String, onNavigateTo: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(top = 24.dp, start = 20.dp)
            .clip(shape = RoundedCornerShape(24.dp))
            .background(color = Color(0xFF898FEC))
            .clickable { onNavigateTo() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(30.dp)
        ) {
            Text(
                gameText,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(vertical = 24.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.undraw_team_up_qeem_1),
                contentDescription = null
            )
        }
    }
}



