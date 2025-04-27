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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.support.presentation.screens.mainScreens.LeaderboardScreen
import com.example.support.presentation.screens.gameScreens.FirstGameScreen
import com.example.support.presentation.screens.authScreens.LoginScreen
import com.example.support.presentation.screens.mainScreens.MainMenuScreen
import com.example.support.presentation.screens.mainScreens.ProfileScreen
import com.example.support.presentation.screens.other.SeeMoreScreen
import com.example.support.presentation.screens.authScreens.RegisterScreen
import com.example.support.presentation.screens.gameScreens.FourthGameScreen
import com.example.support.presentation.screens.gameScreens.SecondGameScreen
import com.example.support.presentation.screens.gameScreens.ThirdGameScreen
import com.example.support.presentation.screens.other.GameCompleteScreen
import com.example.support.presentation.viewModels.MainMenuViewModel
import com.example.support.presentation.viewModels.authViewModels.AuthViewModel
import com.example.support.presentation.ui.component.BottomNavigationBar

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    data object Register : Screen("register")
    @Serializable
    data object Login : Screen("login")
    @Serializable
    data object FirstGame : Screen("first_game")
    @Serializable
    data object SecondGame : Screen("second_game")
    @Serializable
    data object ThirdGame : Screen("third_game")
    @Serializable
    data object FourthGame : Screen("fourth_game")
    @Serializable
    data object MainMenu : Screen("main_menu")
    @Serializable
    data object Profile : Screen("profile")
    @Serializable
    data object SeeMore : Screen("see_more")
    @Serializable
    data object Leaderboard : Screen("leaderboard")
    @Serializable
    data object GameComplete : Screen("game_complete")
}

@Composable
fun MainNav(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    mainMenuViewModel: MainMenuViewModel = hiltViewModel()
) {
    val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsState()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // List of screens where BottomNavigationBar should be displayed
    val screensWithBottomNav = listOf(
        Screen.Leaderboard.route,
        Screen.MainMenu.route,
        Screen.Profile.route
    )

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (currentRoute in screensWithBottomNav) {
            BottomNavigationBar(navHostController)
        }}
    ) { paddingValues ->
        NavHost(
            modifier = modifier.padding(paddingValues),
            navController = navHostController,
            startDestination = if (isUserLoggedIn) Screen.MainMenu.route else Screen.Login.route
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onNavigateTo = { navigateTo ->
                        navHostController.navigate(navigateTo) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onExitGame = {
                        navHostController.navigate(Screen.MainMenu.route) {
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
                    },
                    onExitGame = {
                        navHostController.navigate(Screen.MainMenu.route) {
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
            composable(Screen.ThirdGame.route) {
                ThirdGameScreen(
                    onNavigateTo = { navigateTo ->
                        navHostController.navigate(navigateTo)
                    },
                    onExitGame = {
                        navHostController.navigate(Screen.MainMenu.route) {
                            popUpTo(Screen.ThirdGame.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.FourthGame.route) {
                FourthGameScreen(
                    onNavigateTo = { navigateTo ->
                        navHostController.navigate(navigateTo)
                    },
                    onExitGame = {
                        navHostController.navigate(Screen.MainMenu.route) {
                            popUpTo(Screen.FourthGame.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.MainMenu.route) {
                MainMenuScreen(
                    onNavigateTo = { navigateTo ->
                        navHostController.navigate(navigateTo)
                    },
                    viewModel = mainMenuViewModel
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateTo = { navigateTo ->
                        navHostController.navigate(navigateTo)
                    },
                    onExitGame = {
                        navHostController.navigate(Screen.MainMenu.route) {
                            popUpTo(Screen.Profile.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Leaderboard.route) {
                LeaderboardScreen()
            }
            composable(Screen.SeeMore.route) {
                SeeMoreScreen(
                    onNavigateTo = { navigateTo ->
                        navHostController.navigate(navigateTo)
                    },
                    onExitGame = {
                        navHostController.navigate(Screen.MainMenu.route) {
                            popUpTo(Screen.SeeMore.route) { inclusive = true }
                        }
                    },
                    viewModel=mainMenuViewModel
                )

            }
            composable("${Screen.GameComplete.route}/{gameType}") { backStackEntry ->
                val gameType = backStackEntry.arguments?.getString("gameType") ?: "first_game"

                GameCompleteScreen(
                    viewModel = mainMenuViewModel,
                    onNavigateTo = { navHostController.navigate(it) },
                    onResetGame = {
                        when (gameType) {
                            "first_game" -> navHostController.popBackStack(Screen.FirstGame.route, inclusive = true)
                            "second_game" -> navHostController.popBackStack(Screen.SecondGame.route, inclusive = true)
                            "third_game" -> navHostController.popBackStack(Screen.ThirdGame.route, inclusive = true)
                            "fourth_game" -> navHostController.popBackStack(Screen.FourthGame.route, inclusive = true)
                        }
                    },
                    gameType = gameType
                )
            }



        }
    }
}