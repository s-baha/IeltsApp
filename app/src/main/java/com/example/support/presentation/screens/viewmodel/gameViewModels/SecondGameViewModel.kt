package com.example.support.presentation.screens.viewmodel.gameViewModels

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.support.data.repository.SecondGameRepository
import com.example.support.domain.entity.SecondGame
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.*
import com.example.support.data.repository.AuthRepository
import com.example.support.data.repository.RatingRepository
import com.example.support.domain.entity.User
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await

@HiltViewModel
class SecondGameViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val repository: SecondGameRepository,
    private val database: DatabaseReference,
    private val ratingRepository: RatingRepository
) : ViewModel() {

    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _currentQuestion = mutableStateOf<SecondGame?>(null)
    val currentQuestion: State<SecondGame?> = _currentQuestion

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
            loadNewQuestion()
            repository.insertInitialData()
        }
    }

    // Load user data from Firebase
    private fun loadUser() {
        val userId = authRepository.getLoggedInUserId() ?: return
        database.child("users").child(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                _user.value = user
                _score.intValue = user?.score ?: 0
                _rank.intValue = user?.rank ?: 0
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = exception.message
            }
    }

    // Loads new question from the repository
    private fun loadNewQuestion() {
        viewModelScope.launch {
            _currentQuestion.value = repository.getRandomQuestion()
        }
    }

    // Clears the error message
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Checks the user's answer against the correct answer
    fun checkAnswer(userAnswer: String, context: Context) {
        val correctAnswer = _currentQuestion.value?.answer ?: return

        if (userAnswer.equals(correctAnswer, ignoreCase = true)) {
            updateUserScore()
        } else {
            vibrateDevice(context)
        }
        loadNewQuestion()
    }

    // Updates the user's score in the database
    private fun updateUserScore() {
        val userId = authRepository.getLoggedInUserId() ?: return
        val newScore = (_score.intValue + 10)

        database.child("users").child(userId).updateChildren(mapOf("score" to newScore))
            .addOnSuccessListener {
                updateUserRanks()
            }
        _score.intValue = newScore
    }

    private fun updateUserRanks() {
        viewModelScope.launch {
            ratingRepository.updateUserRanks()
            val userId = authRepository.getLoggedInUserId() ?: return@launch
            val updatedUser = ratingRepository.getUser(userId)
            _rank.intValue = updatedUser?.rank ?: _rank.intValue
        }
    }


    // Vibrates the device
    private fun vibrateDevice(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            val effect = VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        }
    }

    // Resets the game state
    fun resetGame() {
        _currentQuestion.value = null
    }

}
