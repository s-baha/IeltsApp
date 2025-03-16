package com.example.support.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.support.R
import com.example.support.domain.entity.User
import com.example.support.presentation.screens.viewModels.RatingViewModel


@Composable
fun LeaderboardScreen(
    viewModel: RatingViewModel = hiltViewModel()
) {
    val rankingList by viewModel.rankingList.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF898FEC), Color(0xFF595D99))
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            LeaderboardHeader()

            // Sort by score
            val sortedList = rankingList.sortedByDescending { it.score }

            // Top 3
            if (sortedList.isNotEmpty()) {
                LeaderboardTop3(sortedList)
            }

            // Other users
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF4B4E78))
                    .padding(top = 12.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(sortedList.drop(3)) { index, entry ->
                    RegularLeaderboardItem(entry.copy(rank = index + 4))
                }
            }
            Text(text = "Users count: ${rankingList.size}", color = Color.White, fontSize = 20.sp)

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
            modifier = Modifier
                .size(73.dp)
                .clip(CircleShape)
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
@Composable
fun LeaderboardHeader() {
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Leader Board",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE6D8F8)
            )
        }
    }
}
@Composable
fun LeaderboardTop3(sortedList: List<User>) {
    if (sortedList.size < 3) return // Check if there are at least 3 players

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(358.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val second = sortedList.getOrNull(1)
        val first = sortedList.getOrNull(0)
        val third = sortedList.getOrNull(2)

        LeaderboardTopItem(second,  146.dp)
        LeaderboardTopItem(first,  214.dp)
        LeaderboardTopItem(third,  97.dp)
    }
}

@Composable
fun LeaderboardTopItem(user: User?, height: Dp) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Player Avatar",
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .size(101.dp, height)
                .clip(RoundedCornerShape(27.dp))
                .background(Color(0xFF595D99)),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = user?.username ?: "N/A",
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "${user?.score ?: 0} Pts",
                fontSize = 14.sp,
                color = Color.LightGray,
                modifier = Modifier.padding(top = 36.dp)
            )
        }
    }
}
