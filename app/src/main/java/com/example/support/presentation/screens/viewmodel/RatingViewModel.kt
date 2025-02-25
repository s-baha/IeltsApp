package com.example.support.presentation.screens.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.support.data.repository.AuthRepository
import com.example.support.data.repository.FirstGameRepository
import com.example.support.data.repository.RatingRepository
import com.example.support.domain.entity.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
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
            loadUsers()
        }
    }

    private suspend fun loadUsers() {
        val users = ratingRepository.getUsersByRating()
        _rankingList.value = users
    }
}
