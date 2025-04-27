package com.example.support.presentation.viewModels.gameViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.support.data.repository.FourthGameRepository
import com.example.support.domain.entity.FourthGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FourthGameViewModel @Inject constructor(
    private val repository: FourthGameRepository
) : ViewModel() {

    var score = mutableStateOf(0)
        private set

    var wrongCatches = mutableStateOf(0)
        private set

    var timeLeft = mutableStateOf(48)
        private set

    var isPaused = mutableStateOf(false)
        private set

    var isGameOver = mutableStateOf(false)
        private set

    var selectedWordData = mutableStateOf(FourthGame())
        private set

    var wordsList = mutableStateOf(listOf<String>())
        private set

    var restartKey = mutableStateOf(0)
        private set

    init {
        restartGame()
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (timeLeft.value > 0 && !isGameOver.value) {
                if (!isPaused.value) {
                    delay(1000L)
                    timeLeft.value -= 1

                    if (timeLeft.value % 16 == 0 && timeLeft.value > 0) {
                        loadNewWords()
                    }
                } else {
                    delay(100L)
                }
            }
            if (timeLeft.value <= 0) {
                isGameOver.value = true
            }
        }
    }

    private fun loadNewWords() {
        viewModelScope.launch {
            val wordData = repository.getRandomWordData()
            if (wordData != null) {
                selectedWordData.value = wordData
                wordsList.value = (wordData.synonyms + wordData.otherWords)
                    .shuffled()
                    .take(5)
            } else {
                Log.e("FourthGameViewModel", "Нет данных в Firebase!")
            }
        }
    }

    private fun insertInitialDataOnce() {
        repository.insertInitialData()
    }

    init {
        viewModelScope.launch {
            insertInitialDataOnce()
        }
    }
    fun catchWord(word: String) {
        val correct = selectedWordData.value.synonyms.contains(word)

        if (correct) {
            score.value += 1
        } else {
            wrongCatches.value += 1
            if (wrongCatches.value >= 3) {
                isGameOver.value = true
            }
        }

        wordsList.value = wordsList.value.filter { it != word }
    }

    fun togglePause() {
        isPaused.value = !isPaused.value
    }

    fun restartGame() {
        viewModelScope.launch {
            score.value = 0
            wrongCatches.value = 0
            timeLeft.value = 48
            isGameOver.value = false
            loadNewWords()
            restartKey.value += 1
            startTimer()
        }
    }
}
