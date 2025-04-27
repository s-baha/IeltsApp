package com.example.support.presentation.viewModels.gameViewModels

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.support.data.repository.SecondGameRepository
import com.example.support.domain.entity.SecondGame
import kotlinx.coroutines.*
import javax.inject.Inject
import androidx.compose.runtime.*
import com.example.support.data.repository.AuthRepository
import com.example.support.data.repository.RatingRepository
import com.example.support.domain.entity.User
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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

    private val _score = mutableStateOf(0)
    val score: State<Int> = _score

    private val _rank = mutableStateOf(0)
    val rank: State<Int> = _rank

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _timeLeft = MutableStateFlow(30) // Use StateFlow
    val timeLeft: StateFlow<Int> = _timeLeft


    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            loadUser()
            loadNewQuestion()
            repository.insertInitialData()
        }
    }

    fun startTimer(onNavigateToGameComplete: () -> Unit) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000L)
                _timeLeft.value = _timeLeft.value - 1
            }
            onNavigateToGameComplete() // Когда таймер заканчивается, переходим на экран завершения
        }
    }


    private suspend fun loadUser() {
        val userId = authRepository.getLoggedInUserId() ?: return
        try {
            val user = suspendCoroutine<User?> { continuation ->
                database.child("users").child(userId).get()
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(snapshot.getValue(User::class.java))
                    }
                    .addOnFailureListener { exception ->
                        _errorMessage.value = exception.message
                        continuation.resume(null)
                    }
            }
            user?.let {
                _user.value = it
                _score.value = it.score ?: 0
                _rank.value = it.rank ?: 0
            }
        } catch (e: Exception) {
            _errorMessage.value = e.message
        }
    }

    private fun loadNewQuestion() {
        viewModelScope.launch {
            _currentQuestion.value = repository.getRandomQuestion()
        }
    }

    fun checkAnswer(userAnswer: String, context: Context) {
        val correctAnswer = _currentQuestion.value?.answer ?: return

        if (userAnswer.equals(correctAnswer, ignoreCase = true)) {
            updateUserScore()
        } else {
            vibrateDevice(context)
        }

        loadNewQuestion()
    }

    private fun updateUserScore() {
        val userId = authRepository.getLoggedInUserId() ?: return
        val newScore = _score.value + 10

        database.child("users").child(userId).updateChildren(mapOf("score" to newScore))
            .addOnSuccessListener {
                _score.value = newScore
                updateUserRanks()
            }
    }

    private fun updateUserRanks() {
        viewModelScope.launch {
            ratingRepository.updateUserRanks()
            val userId = authRepository.getLoggedInUserId() ?: return@launch
            val updatedUser = ratingRepository.getUser(userId)
            _rank.value = updatedUser?.rank ?: _rank.value
        }
    }

    private fun vibrateDevice(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return
        if (vibrator.hasVibrator()) {
            val effect = VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun resetGame() {
        _score.value = 0
        _timeLeft.value = 30
        loadNewQuestion()
        startTimer { /* обработка окончания игры */ }
    }
}
