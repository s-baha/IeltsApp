package com.example.support.presentation.screens.viewModels.authViewModels

import androidx.lifecycle.ViewModel
import com.example.support.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun logout() {
        authRepository.logout()
        _isUserLoggedIn.value = false
    }
}
