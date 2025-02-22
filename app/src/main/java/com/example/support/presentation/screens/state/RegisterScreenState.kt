package com.example.support.presentation.screens.state

import com.example.support.util.Result

sealed class RegisterScreenEvent {
    data class UsernameUpdated(val newUsername:String): RegisterScreenEvent()
    data class EmailUpdated(val newEmail:String): RegisterScreenEvent()
    data class PasswordUpdated(val newPassword:String): RegisterScreenEvent()
    data object RegisterButtonClicked: RegisterScreenEvent()
}

data class RegisterScreenState(
    val username: String="",
    val email: String="",
    val password: String="",
    val registerResult: Result? = null
)