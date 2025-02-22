package com.example.support.presentation.screens.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.support.data.repository.AuthRepository
import com.example.support.presentation.screens.state.LoginScreenEvent
import com.example.support.presentation.screens.state.LoginScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.support.util.Result


@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) :ViewModel (){
    var state by mutableStateOf(LoginScreenState())
        private set

    // Handle events
    fun onEvent(event: LoginScreenEvent){
        when(event){
            is LoginScreenEvent.EmailUpdated ->{
                this.state = state.copy(email = event.newEmail)
            }
            is LoginScreenEvent.PasswordUpdated ->{
                this.state = state.copy(password = event.newPassword)
            }
            is LoginScreenEvent.LoginButtonClicked ->login()
        }
    }
    // User authorization
    private fun login() = viewModelScope.launch {
        val email = state.email
        val password = state.password
        if (email.isEmpty() || password.isEmpty()) return@launch

        authRepository.login(email, password) { success, message ->
            state = state.copy(
                loginResult = if (success) {
                    Result.Success<String>(message)
                } else {
                    Result.Failure<String>(message)
                }
            )
        }
    }
}