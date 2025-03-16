package com.example.support.presentation.screens.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.support.data.repository.AuthRepository
import com.example.support.domain.entity.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val database: DatabaseReference
): ViewModel()  {

    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _score = mutableIntStateOf(0)
    val score: State<Int> = _score

    private val _rank = mutableIntStateOf(0)
    val rank: State<Int> = _rank

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // Initialize the ViewModel
    init {
        viewModelScope.launch {
            loadUser()
        }
    }
    // Load user data from Firebase
    private fun loadUser() {
        val userID = authRepository.getLoggedInUserId() ?: return

        database.child("users").child(userID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    _user.value = user
                    _score.intValue = user?.score ?: 0
                    _rank.intValue = user?.rank ?: 0
                }

                override fun onCancelled(error: DatabaseError) {
                    _errorMessage.value = "Ошибка загрузки данных: ${error.message}"
                }
            })

        // Clears the error message
        fun clearErrorMessage() {
            _errorMessage.value = null
        }

    }}