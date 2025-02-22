package com.example.support.presentation.screens.state

import com.example.support.util.Result

sealed class LoginScreenEvent {
    data class EmailUpdated(val newEmail: String): LoginScreenEvent()
    data class PasswordUpdated(val newPassword: String): LoginScreenEvent()
    data object LoginButtonClicked : LoginScreenEvent()
}

data class LoginScreenState(
    val email: String = "",
    val password: String = "",
    val loginResult: Result? = null
)