package com.example.support.presentation.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.support.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn

    init {
        checkUserSession()
    }

    private fun checkUserSession() {
        _isUserLoggedIn.value = authRepository.getCurrentUser() != null
    }

    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            authRepository.login(email, password) { success, message ->
                if (success) {
                    _isUserLoggedIn.value = true
                }
                callback(success, message)
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _isUserLoggedIn.value = false
    }
}
