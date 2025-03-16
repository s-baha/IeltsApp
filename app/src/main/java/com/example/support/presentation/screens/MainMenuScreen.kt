package com.example.support.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.support.R
import com.example.support.presentation.navigation.Screen
import com.example.support.presentation.screens.viewModels.MainMenuViewModel
import com.example.support.presentation.ui.component.RowGames

@Composable
fun MainMenuScreen(
    onNavigateTo: (String) -> Unit,
    viewModel: MainMenuViewModel
) {
        MainPage(onNavigateTo = onNavigateTo, viewModel = viewModel)
}

@Composable
fun MainPage(
    viewModel: MainMenuViewModel,
    onNavigateTo: (String) -> Unit
){
    val user = viewModel.user.value?.username.toString()
    val score = viewModel.score.value
    val rank = viewModel.rank.value


    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize().padding()
            .background(color = Color(0xFF4B4E78))
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
        ,
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(
                bottomStart = 24.dp,
                bottomEnd = 24.dp)
            )
        ) {
            Column {
                ProfileBox(user, score,rank)
                RowGames(onNavigateTo)
                LazyRow(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val games = listOf(
                        "Fact Or Opinions" to Screen.SecondGame.route,
                        "Phrasal Verbs" to Screen.FirstGame.route,
                        "Choose Keywords" to Screen.ThirdGame.route
                    )

                    items(games) { (gameName, route) ->
                        GamesCard(gameName) {
                            onNavigateTo(route)
                        }
                    }
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
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                gameText,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(vertical = 20.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.undraw_team_up_qeem_1),
                contentDescription = null
            )
        }
    }
}