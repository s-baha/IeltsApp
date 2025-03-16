package com.example.support.presentation.screens.viewModels.authViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.support.data.repository.AuthRepository
import com.example.support.presentation.screens.state.RegisterScreenEvent
import com.example.support.presentation.screens.state.RegisterScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.support.util.Result



@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    var state by mutableStateOf(RegisterScreenState())
        private set

    // Handle events
    fun onEvent(event: RegisterScreenEvent){
        when(event){
            is RegisterScreenEvent.UsernameUpdated ->{
                state = state.copy(username = event.newUsername)
            }
            is RegisterScreenEvent.EmailUpdated ->{
                state = state.copy(email = event.newEmail)
            }
            is RegisterScreenEvent.PasswordUpdated ->{
                state = state.copy(password = event.newPassword)
            }
            is RegisterScreenEvent.RegisterButtonClicked -> register()
        }
    }

    // User registration
    private fun register() = viewModelScope.launch {
        val username = state.username
        val email = state.email
        val password = state.password
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) return@launch

        authRepository.register(email, password, username) { success, message ->
            state = state.copy(
                registerResult = if (success) {
                    Result.Success<String>(message)
                } else {
                    Result.Failure<String>(message)
                }
            )
        }
    }
}