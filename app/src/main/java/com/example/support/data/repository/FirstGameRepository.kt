package com.example.support.data.repository

import com.example.support.domain.entity.FirstGame
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirstGameRepository @Inject constructor(
    private val database: DatabaseReference
) {
    suspend fun getRandomQuestion(): FirstGame? = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            database.child("games").child("firstGame").get()
                .addOnSuccessListener { snapshot ->
                    val questions = snapshot.children.mapNotNull { it.getValue(FirstGame::class.java) }
                    val randomQuestion = questions.randomOrNull()
                    continuation.resume(randomQuestion)
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
    }



     fun insertInitialData() {
        val questions = listOf(
            FirstGame(id=1, "She is ", "getting ready", "to her meeting."),
            FirstGame(id=2, "He ", "caught up with", "his old school friends at the reunion last weekend."),
            FirstGame(id=3, "We need to ", "come up with", "a new strategy to solve this problem."),
            FirstGame(id=4, "She was so tired that she ", "dozed off", "during the lecture."),
            FirstGame(id=5, "They had to ", "call off", "the meeting because the manager was sick."),
            FirstGame(id=6, "I can't ", "figure out", "how to use this software. Can you help me?"),
            FirstGame(id=7, "The car suddenly ", "broke down", "in the middle of the road."),
            FirstGame(id=8, "He didn’t want to go to the party, but his friends  ", "talked into", "him"),
        )
        questions.forEach { question ->
            database.child("games").child("firstGame").child(question.id.toString()).setValue(question)
        }
    }



}
