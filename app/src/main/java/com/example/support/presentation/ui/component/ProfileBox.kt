package com.example.support.presentation.ui.component

import androidx.compose.runtime.Composable
import android.text.Layout.Alignment
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.twotone.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.support.R
import com.example.support.presentation.ui.component.UserStatsPanel

@Composable
fun ProfileBox(
    username:String,
    score: Int,
    rank: Int
){
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(
                bottomEnd = 20.dp,
                bottomStart = 20.dp
            ))
            .background(color = Color(0xFF595D99))
            .padding(top = 45.dp, bottom = 20.dp)
    ) {
        Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp)

            ) {
                Text(
                    username,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.W400,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.padding(vertical = 5.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(color = Color(0xFFE7E8FF))
                    .padding(vertical = 7.dp)
            ) {
                Column {
                    Text("Ranking")
                    HowPoints(rank)
                }
                Column {
                    Text("Points")
                    HowPoints(score)
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 10.dp))

            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(color = Color(0xFFE7E8FF))
                    .padding(horizontal = 30.dp)
            ){
                Column(modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(vertical = 20.dp)
                ) {
                    ProfileImage(
                        R.drawable.ic_launcher_foreground
                    )
                    Spacer(modifier = Modifier.padding(bottom = 12.dp))
                    Text(username,
                        fontWeight = FontWeight.W600,
                        fontSize = 28.sp,
                        color = Color(0xFF120321)
                    )
                    Spacer(modifier = Modifier.padding(bottom = 12.dp))

                    EditButton()
                }
            }
        }



    }
}

@Composable
fun EditButton(){
    Button(onClick = {},) {
        Icon(
            painter = rememberVectorPainter(image = Icons.Default.Edit),
            contentDescription = null,
            modifier = Modifier.size(10.dp)
        )
        Text(" Edit profile", fontSize = 10.sp)

    }
}