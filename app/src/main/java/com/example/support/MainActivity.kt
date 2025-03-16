package com.example.support

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.support.presentation.navigation.MainNav
import com.example.support.presentation.screens.MainPage
import com.example.support.presentation.screens.gameScreens.ThirdGameScreen
import com.example.support.presentation.theme.SupportTheme
import com.google.firebase.Firebase
import com.google.firebase.database.database
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           MainContent()
        }
    }
}

@Composable
fun MainContent(
){
    val navController = rememberNavController()
    MainNav(navHostController = navController)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SupportTheme {
        MainContent()
    }
}