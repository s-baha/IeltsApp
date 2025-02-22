package com.example.support.presentation.screens.viewmodel.gameViewModels

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.support.data.repository.AuthRepository
import com.example.support.data.repository.SecondGameRepository
import com.example.support.data.repository.ThirdGameRepository
import com.example.support.domain.entity.ThirdGame
import com.example.support.domain.entity.User
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThirdGameViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val repository: ThirdGameRepository,
    private val database: DatabaseReference
) : ViewModel() {
    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _score = mutableIntStateOf(0)
    val score: State<Int> = _score

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _sentence = MutableStateFlow<ThirdGame?>(null)
    val sentence: StateFlow<ThirdGame?> = _sentence

    private val _selectedWords = MutableStateFlow<List<String>>(emptyList())
    val selectedWords: StateFlow<List<String>> = _selectedWords

    private val _isChecked = mutableStateOf(false)
    val isChecked: State<Boolean> = _isChecked

    private val _isCorrectAnswer = mutableStateOf<Boolean?>(null)
    val isCorrectAnswer: State<Boolean?> = _isCorrectAnswer



    // Initialize the ViewModel
    init {
        viewModelScope.launch {
            loadUser()
            repository.insertInitialData()
        }
    }

    fun loadRandomSentence() {
        viewModelScope.launch {
            val newSentence = repository.getRandomSentence()
            if (newSentence == null) {
                _errorMessage.value = "Ошибка: не удалось загрузить предложение"
            }
            _sentence.value = newSentence
            _selectedWords.value = emptyList() // Очищаем выбор пользователя
        }
    }


    fun selectWord(word: String) {
        _selectedWords.value = _selectedWords.value.toMutableList().apply {
            if (contains(word)) remove(word) else add(word)
            Log.d("ThirdGame", "Выбранные слова: $_selectedWords")
        }
    }


    fun isCorrectWord(word: String): Boolean {
        return _sentence.value?.answers?.map { it.lowercase() }?.contains(word.lowercase()) ?: false
    }


    fun checkAnswer(context: Context) {
        val correctAnswers = _sentence.value?.answers?.map { it.lowercase() }?.sorted() ?: emptyList()
        val userAnswers = _selectedWords.value.map { it.lowercase() }.sorted()

        if (correctAnswers == userAnswers) {
            // ✅ Если все выбраны правильно – окрашиваем в зелёный
            _isCorrectAnswer.value = true
            updateUserScore()
            viewModelScope.launch {
                delay(2000) // Даем игроку увидеть правильный ответ
                _isCorrectAnswer.value = null // Сбрасываем цвет
                loadRandomSentence()
            }
        } else {
            // ❌ Если есть ошибки – окрашиваем в красный
            _isCorrectAnswer.value = false
            vibrateDevice(context)
            viewModelScope.launch {
                delay(2000) // Даем игроку увидеть ошибку
                _isCorrectAnswer.value = null // Сбрасываем цвет
                _selectedWords.value = emptyList() // Сбрасываем выбор
            }
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
            }
    }

    // Clears the error message
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Updates the user's score in the database
    private fun updateUserScore() {
        val userId = authRepository.getLoggedInUserId() ?: return
        val newScore = (_score.intValue + 10)

        database.child("users").child(userId).updateChildren(mapOf("score" to newScore))
            .addOnSuccessListener {
                _score.intValue = newScore // Локально обновляем счет только если Firebase успешно обновил
            }
            .addOnFailureListener { error ->
                _errorMessage.value = "Ошибка обновления счета: ${error.message}"
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

    fun resetGame() {
        _selectedWords.value = emptyList()
    }

}