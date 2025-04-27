package com.example.support.presentation.screens.mainScreens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.support.R
import com.example.support.domain.entity.User
import com.example.support.presentation.screens.state.UiState
import com.example.support.presentation.viewModels.RatingViewModel

@Composable
fun LeaderboardScreen(
    viewModel: RatingViewModel = hiltViewModel()
) {
    val rankingList by viewModel.rankingList.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.observeUsers()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF898FEC), Color(0xFF595D99))
                )
            )
    ) {
        when (uiState) {
            is UiState.Loading -> CircularProgressIndicator(color = Color.White)
            is UiState.Success -> {
                val sortedList = rankingList.sortedByDescending { it.score }

                Column(modifier = Modifier.fillMaxSize()) {
                    TopBackgroundSection(sortedList)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 12.dp, bottom = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(sortedList.drop(3)) { index, entry ->
                            RegularLeaderboardItem(entry.copy(rank = index + 4))
                        }
                    }
                    Text(
                        text = "Users count: ${rankingList.size}",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
            is UiState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Ошибка: ${(uiState as UiState.Error).message}",
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.observeUsers() }) {
                        Text("Попробовать снова")
                    }
                }
            }
        }
    }
}

@Composable
fun TopBackgroundSection( sortedList: List<User>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(428.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF898FEC), Color(0xFF595D99))
                ),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .offset(x = 329.dp, y = 20.dp)
                .size(width = 245.dp, height = 240.dp)
                .clip(RoundedCornerShape(200.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFA8B2F5), Color(0xFF6A70B0))
                    )
                )
        )
        Box(
            modifier = Modifier
                .offset(x = (-93).dp, y = (-100).dp)
                .size(width = 245.dp, height = 240.dp)
                .clip(RoundedCornerShape(200.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFA8B2F5), Color(0xFF6A70B0))
                    )
                )
        )
        Header()
        if (sortedList.size >= 3) {
            LeaderboardTop3(sortedList)
        }
    }
}

@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(top = 40.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Leader Board",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE6D8F8)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun LeaderboardTop3(sortedList: List<User>) {
    val (first, second, third) = listOf(
        sortedList.getOrNull(0),
        sortedList.getOrNull(1),
        sortedList.getOrNull(2)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(358.dp)
,        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LeaderboardTopItem(second, 146.dp, rank = 2)
        LeaderboardTopItem(first, 214.dp, rank = 1)
        LeaderboardTopItem(third, 97.dp, rank = 3)
    }
}

@Composable
fun LeaderboardTopItem(user: User?, height: Dp, rank: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Player Avatar",
            modifier = Modifier.size(58.dp).clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .size(101.dp, height)
                .clip(RoundedCornerShape(27.dp))
                .background(Color(0xFF595D99)),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "$rank", fontSize = 30.sp, color = Color(0xFFA8B2F5), modifier = Modifier.padding(top = 8.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = user?.username ?: "N/A", fontSize = 14.sp, color = Color.White)
                Text(text = "${user?.score ?: 0} pts", fontSize = 12.sp, color = Color.LightGray)
            }
        }
    }
}

@Composable
fun RegularLeaderboardItem(entry: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(27.dp))
            .background(Color(0xFF595D99))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = entry.rank.toString(),
            fontSize = 30.sp,
            color = Color(0xFFA8B2F5),
            modifier = Modifier.width(30.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Player Avatar",
            modifier = Modifier.size(73.dp).clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "${entry.username} - ${entry.score} Points",
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
    }
}
