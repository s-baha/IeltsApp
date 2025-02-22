package com.example.support.presentation.navigation

import kotlinx.serialization.Serializable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.support.presentation.screens.gameScreens.FirstGameScreen
import com.example.support.presentation.screens.LoginScreen
import com.example.support.presentation.screens.MainMenuScreen
import com.example.support.presentation.screens.ProfileScreen
import com.example.support.presentation.screens.RatingScreen
import com.example.support.presentation.screens.RegisterScreen
import com.example.support.presentation.screens.gameScreens.SecondGameScreen
import com.example.support.presentation.screens.viewmodel.AuthViewModel
import com.example.support.presentation.ui.component.BottomNavBar

@Serializable
sealed class Screen(val route: String, val title: String) {
    @Serializable
    data object Register : Screen("register", "Register")
    @Serializable
    data object Login : Screen("login", "Login")
    @Serializable
    data object FirstGame : Screen("first_game", "First Game")
    @Serializable
    data object SecondGame : Screen("second_game", "Second Game")
    @Serializable
    data object MainMenu : Screen("main_menu", "Main Menu")
    @Serializable
    data object Profile : Screen("profile", "Profile")
    @Serializable
    data object Rating : Screen("rating", "Rating")
}

@Composable
fun MainNav(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsState()


    NavHost(
        modifier = modifier.padding(),
        navController = navHostController,
        // startDestination = Screen.Login.route
        startDestination = if (isUserLoggedIn) Screen.MainMenu.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateTo = { navigateTo ->
                    navHostController.navigate(navigateTo) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateTo = { navigateTo ->
                    navHostController.navigate(navigateTo) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.FirstGame.route) {
            FirstGameScreen(
                onNavigateTo = { navigateTo ->
                    navHostController.navigate(navigateTo)
                },
                onExitGame = {
                    navHostController.navigate(Screen.MainMenu.route) {
                        popUpTo(Screen.FirstGame.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.SecondGame.route) {
            SecondGameScreen(
                onNavigateTo = { navigateTo ->
                    navHostController.navigate(navigateTo)
                },
                onExitGame = {
                    navHostController.navigate(Screen.MainMenu.route) {
                        popUpTo(Screen.SecondGame.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.MainMenu.route) {
            MainMenuScreen(
                onNavigateTo = { navigateTo ->
                    navHostController.navigate(navigateTo) {
                        popUpTo(Screen.MainMenu.route) {}
                    }
                },
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateTo = { navigateTo ->
                    navHostController.navigate(navigateTo)
                }
            )
        }
        composable(Screen.Rating.route) {
            RatingScreen(
//                onNavigateTo = { navigateTo ->
//                    navHostController.navigate(navigateTo)
//                }
            )
        }


    }
}