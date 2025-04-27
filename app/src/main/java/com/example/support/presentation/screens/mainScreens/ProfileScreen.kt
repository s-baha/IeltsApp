package com.example.support.presentation.screens.mainScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.support.presentation.navigation.Screen
import com.example.support.presentation.viewModels.authViewModels.AuthViewModel

@Composable
fun ProfileScreen(
    onNavigateTo: (String) ->Unit,
    onExitGame: () -> Unit
    ) {
    ProfileView(onNavigateTo=onNavigateTo)
}

@Composable
fun ProfileView(
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateTo: (String) ->Unit
){
Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center

) {
    Button(onClick = {
        authViewModel.logout()
        onNavigateTo(Screen.Login.route)
    }) {
        Text("Log out")
    }
}
}