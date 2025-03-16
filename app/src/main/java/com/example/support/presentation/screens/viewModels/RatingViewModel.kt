package com.example.support.presentation.screens.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.support.data.repository.RatingRepository
import com.example.support.domain.entity.User
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

    init {
        observeUsers()
    }

    private fun observeUsers() {
        viewModelScope.launch {
            ratingRepository.updateUserRanks()
            delay(500) // Give some time for the ranks to be updated
            loadUsers() // Loading users after ranks are updated
        }
    }


    private suspend fun loadUsers() {
        val users = ratingRepository.getUsersByRating()
        _rankingList.value = users
        Log.d("RatingViewModel", "Updated rankingList with ${users.size} users") // 👀
    }

}
