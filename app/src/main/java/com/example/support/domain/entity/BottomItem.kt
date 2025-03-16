package com.example.support.domain.entity

import com.example.support.presentation.navigation.Screen

data class BottomNavItem(
    val title: String,
    val screen: Screen,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)