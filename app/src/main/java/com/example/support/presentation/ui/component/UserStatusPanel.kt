package com.example.support.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.support.R

@Composable
fun UserStatsPanel(
    username:String,
    score: Int
){
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(
                bottomEnd = 20.dp,
                bottomStart = 20.dp
            ))
            .background(color = Color(0xFF595D99))
            .padding(vertical = 45.dp)
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)

            ) {
                ProfileName(username)

                ProfileImage(
                    R.drawable.ic_launcher_background
                )
            }

            Spacer(modifier = Modifier.padding(vertical = 5.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF),
                            Color(0x0FFFFFFF)
                        )
                    ))
            ) {
                Column {
                    Text("Ranking")
                    HowPoints(0)
                }
                Column {
                    Text("Points")
                    HowPoints(score)
                }
            }
        }
    }
}
@Composable
fun HowPoints(point: Int){
    Text(
        point.toString(),
        fontSize = 30.sp,
        fontWeight = FontWeight.W400,
        color = Color.Unspecified
    )
}
@Composable
fun ProfileName(name: String){
    Text(
        name,
        fontSize = 30.sp,
        fontWeight = FontWeight.W400,
        color = Color.White
    )
}
@Composable
fun ProfileImage(url: Int){
    Image(
        painter = painterResource(url),
        contentDescription = null,
        modifier = Modifier
            .size(60.dp)
            .clip(shape = RoundedCornerShape(50))
    )
}