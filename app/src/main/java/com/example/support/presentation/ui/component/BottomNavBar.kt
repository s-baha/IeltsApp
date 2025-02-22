package com.example.support.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.support.presentation.navigation.Screen

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        Screen.Rating,
        Screen.MainMenu,
        Screen.Profile
    )
Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxWidth().background(color = Color(0xFF4B4E78)).padding(bottom = 10.dp)
    ) {
    BottomNavigation(
        backgroundColor = Color(0xFFE7E8FF),
        contentColor = Color.White,
        modifier = Modifier.fillMaxWidth(0.7f).clip(shape = RoundedCornerShape(24.dp))
    ) {
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(getScreenIcon(screen), contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = navController.currentBackStackEntry?.destination?.route == screen.route,
                onClick = {
                    if (navController.currentBackStackEntry?.destination?.route != screen.route) {
                        navController.navigate(screen.route) {
                            // Очищаем стек назад для предотвращения возврата на предыдущие экраны
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true // Запускаем только один экземпляр экрана
                            restoreState = true // Восстанавливаем состояние экрана
                        }
                    }
                }
            )
        }
    }
}
}
@Composable
fun getScreenIcon(screen: Screen): ImageVector {
    return when (screen) {
        Screen.MainMenu -> Icons.Filled.Home
        Screen.Profile -> Icons.Filled.Person
        Screen.Rating -> Icons.Filled.Star
        else -> Icons.Filled.Home // Можно выбрать иконку по умолчанию
    }
}