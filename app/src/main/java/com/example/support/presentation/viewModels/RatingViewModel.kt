package com.example.support.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.support.data.repository.RatingRepository
import com.example.support.domain.entity.User
import com.example.support.presentation.screens.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val ratingRepository: RatingRepository
) : ViewModel() {

    private val _rankingList = MutableStateFlow<List<User>>(emptyList())
    val rankingList: StateFlow<List<User>> = _rankingList

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        observeUsers()
    }

    fun observeUsers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                ratingRepository.updateUserRanks()
                delay(500) // Ждём обновления данных
                loadUsers()
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }


    private suspend fun loadUsers() {
        val users = ratingRepository.getUsersByRating()
        _rankingList.value = users
    }

}
