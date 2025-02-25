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
import com.example.support.data.repository.ThirdGameRepository
import com.example.support.domain.entity.ThirdGame
import com.example.support.domain.entity.User
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
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

    private val _checkedWords = MutableStateFlow<Map<String, Boolean>?>(null)
    val checkedWords: StateFlow<Map<String, Boolean>?> = _checkedWords

    // Initialize the ViewModel
    init {
        viewModelScope.launch {
            loadUser()
            repository.insertInitialData()
        }
    }

    // Load a random sentence from the database
    fun loadRandomSentence() {
        viewModelScope.launch {
            val newSentence = repository.getRandomSentence()
            if (newSentence == null) {
                _errorMessage.value = "Error: no message"
            }
            _sentence.value = newSentence
            _selectedWords.value = emptyList() // clear selected words
        }
    }

    // Select a word from the sentence
    fun selectWord(word: String) {
        _selectedWords.value = _selectedWords.value.toMutableList().let { list ->
            if (word in list) list - word else list + word
        }
        Log.d("ThirdGame", "Checked words: ${_selectedWords.value}")
    }

    //Check answer
    fun checkAnswer(context: Context) {
        val selectedWords = _selectedWords.value ?: emptyList()

        // If no words are selected, do nothing
        if (selectedWords.isEmpty()) {
            setErrorMessage("Error: no words selected")
            return
        }
        // Convert correct answers to lowercase and sort them
        val correctAnswers = _sentence.value?.answers?.map { it.lowercase() }?.sorted() ?: emptyList()
        val selectedWordsLower = selectedWords.map { it.lowercase() }.sorted()

        // Check if all selected words are correct
        val wordCheckResult = selectedWords.associateWith { it.lowercase() in correctAnswers }
        _checkedWords.value = wordCheckResult

        when {
            // If all words are selected correctly, show success message
            selectedWordsLower == correctAnswers -> {
                viewModelScope.launch {
                    delay(1000) // give user time to check the answer
                    _checkedWords.value = null // reset checked words
                    _selectedWords.value = emptyList() // clear selected words
                    updateUserScore()
                    loadRandomSentence()
                }
            }
            // If only part of the words are selected, show error message
            selectedWordsLower.any { it in correctAnswers } -> {
                setErrorMessage("You selected only part of the correct words!")
                vibrateDevice(context)
                viewModelScope.launch {
                    delay(1000)
                    _checkedWords.value = null
                    _selectedWords.value = emptyList()
                }
            }
            // If no words are selected, show error message
            else -> {
                setErrorMessage("Incorrect answer! Try again.")
                vibrateDevice(context)
                viewModelScope.launch {
                    delay(1000)
                    _checkedWords.value = null
                    _selectedWords.value = emptyList()
                }
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
    // Sets the error message
    private fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

    // Updates the user's score in the database
    private fun updateUserScore() {
        val userId = authRepository.getLoggedInUserId() ?: return
        val newScore = (_score.intValue + 10)

        database.child("users").child(userId).updateChildren(mapOf("score" to newScore))
            .addOnSuccessListener {
                _score.intValue = newScore// update local score
            }
            .addOnFailureListener { error ->
                _errorMessage.value = "Error update score: ${error.message}"
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