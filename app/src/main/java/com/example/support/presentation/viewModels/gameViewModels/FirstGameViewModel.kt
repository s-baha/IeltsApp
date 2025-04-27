package com.example.support.presentation.viewModels.gameViewModels

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.support.data.repository.FirstGameRepository
import com.example.support.domain.entity.FirstGame
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.*
import com.example.support.data.repository.AuthRepository
import com.example.support.data.repository.RatingRepository
import com.example.support.domain.entity.User
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

@HiltViewModel
class FirstGameViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val repository: FirstGameRepository,
    private val database: DatabaseReference,
    private val ratingRepository: RatingRepository
):ViewModel()  {

    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _currentQuestion = mutableStateOf<FirstGame?>(null)
    val currentQuestion: State<FirstGame?> = _currentQuestion

    private val _userAnswer = mutableStateOf("")
    val userAnswer:State<String> = _userAnswer

    private val _score = mutableIntStateOf(0)
    val score: State<Int> = _score

    private val _rank = mutableIntStateOf(0)
    val rank: State<Int> = _rank

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage


    private val _timeLeft = MutableStateFlow(30) // Use MutableStateFlow for timeLeft
    val timeLeft: StateFlow<Int> = _timeLeft

    private var timerJob: Job? = null

    // Initialize the ViewModel
    init {
        viewModelScope.launch {
            loadUser()
            loadNewQuestion()
            checkAndInsertInitialData()
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
    fun stopTimer() {
        timerJob?.cancel()
    }


    // Check if initial data exists in the database and insert it if not
    private suspend fun checkAndInsertInitialData() {
        val snapshot = database.child("games").child("firstGame").get().await()
        if (!snapshot.exists()) {
            repository.insertInitialData()
        }
    }

    // Load user data from Firebase
    fun loadUser() {
        val userID = authRepository.getLoggedInUserId() ?: return

        database.child("users").child(userID).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                    _user.value = user
                    _score.intValue = user?.score ?: 0
                    _rank.intValue = user?.rank ?: 0
            }
    }

    // Loads new question from the repository
    fun loadNewQuestion() {
        viewModelScope.launch {
            val question = repository.getRandomQuestion()
                _currentQuestion.value = question
                _userAnswer.value = ""
        }
        Log.d("FirstGameViewModel", "New question loaded: ${_currentQuestion.value}")
    }

    // Updates the user's answer
    fun onAnswerChange(newAnswer : String){
        _userAnswer.value = newAnswer
    }

    // Checks the user's answer against the correct answer
    fun checkAnswer(context: Context){
        val correctAnswer = _currentQuestion.value?.answer ?: return

        val normalizedUserAnswer = _userAnswer.value.trim().replace("\\s+".toRegex(), " ").lowercase()
        val normalizedCorrectAnswer = correctAnswer.trim().replace("\\s+".toRegex(), " ").lowercase()

        if (normalizedUserAnswer == normalizedCorrectAnswer) {
            updateUserScore()
            loadNewQuestion()
        } else {
            vibrateDevice(context)
            _errorMessage.value = "Incorrect answer! Try again."
        }
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

    // Clears the error message
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Vibrates the device
    private fun vibrateDevice(context: Context){
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()){
            val effect = VibrationEffect.createOneShot(300,VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        }
    }

    // Resets the game state
    fun resetGame() {
        _score.intValue = 0
        _timeLeft.value = 30
        loadNewQuestion()
        startTimer { /* processing when time is up */ }
    }
}