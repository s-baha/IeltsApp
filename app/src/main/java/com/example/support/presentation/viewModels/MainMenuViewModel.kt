package com.example.support.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.support.data.repository.AuthRepository
import com.example.support.domain.entity.User
import com.example.support.presentation.screens.state.MainMenuScreenState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val database: DatabaseReference
) : ViewModel() {

    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _score = mutableIntStateOf(0)
    val score: State<Int> = _score

    private val _rank = mutableIntStateOf(0)
    val rank: State<Int> = _rank

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _lastGameScore = MutableStateFlow(0)
    val lastGameScore: StateFlow<Int> = _lastGameScore

    private var _initialScore = 0
    private val _scoreDifference = MutableStateFlow(0)
    val scoreDifference: StateFlow<Int> = _scoreDifference

    fun setLastGameScore(finalScore: Int) {
        _lastGameScore.value = finalScore
        _scoreDifference.value = finalScore - _initialScore
    }

    var uiState = mutableStateOf<MainMenuScreenState>(MainMenuScreenState.Loading)
        private set

    init {
        loadUser()
    }

     fun loadUser() {
        uiState.value = MainMenuScreenState.Loading  // Показываем загрузку

        val userID = authRepository.getLoggedInUserId() ?: return

        database.child("users").child(userID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        _user.value = user
                        _score.intValue = user.score
                        _rank.intValue = user.rank

                        // Сохраняем очки перед началом игры
                        _initialScore = _score.intValue

                        // Обновляем состояние
                        uiState.value = MainMenuScreenState.Success(user, user.score, user.rank)
                    } else {
                        uiState.value = MainMenuScreenState.Error("Пользователь не найден")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    uiState.value = MainMenuScreenState.Error("Ошибка загрузки: ${error.message}")
                }
            })
    }


    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
