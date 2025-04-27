package com.example.support.presentation.screens.state

import com.example.support.domain.entity.User

sealed class MainMenuScreenState {
    data object Loading : MainMenuScreenState()
    data class Success(val user: User, val score: Int, val rank: Int) : MainMenuScreenState()
    data class Error(val message: String) : MainMenuScreenState()
}
