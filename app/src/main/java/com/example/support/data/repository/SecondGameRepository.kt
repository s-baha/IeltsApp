package com.example.support.data.repository

import com.example.support.domain.entity.SecondGame
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SecondGameRepository @Inject constructor(
    private val database: DatabaseReference
) {

    suspend fun getRandomQuestion(): SecondGame?= withContext(Dispatchers.IO) {
        suspendCoroutine{continuation ->
            database.child("games").child("secondGame").get()
                .addOnSuccessListener {snapshot ->
                    val question = snapshot.children.mapNotNull { it.getValue(SecondGame::class.java) }
                    val randomQuestion = question.randomOrNull()
                    continuation.resume(randomQuestion)
                }
        }
    }

     fun insertInitialData() {
        val questions = listOf(
            SecondGame(text = "Artificial intelligence will eventually replace most human jobs.", answer = "Opinion", id=1),
            SecondGame(text = "The human brain contains approximately 86 billion neurons.", answer = "Fact", id=2),
            SecondGame(text = "Online education is more effective than traditional classroom learning.", answer = "Opinion", id=3),
            SecondGame(text = "The Treaty of Versailles was signed in 1919 and officially ended World War I.", answer = "Fact", id=4),
            SecondGame(text = "Climate change is the most critical issue facing humanity today.", answer = "Opinion", id=5),
            SecondGame(text = "Shakespeare’s works have been translated into over 100 languages.", answer = "Fact", id=6),
            SecondGame(text = "A democratic government is the best form of governance for all societies.", answer = "Opinion", id=7),
            SecondGame(text = "Mount Everest is the highest mountain above sea level, standing at 8,848.86 meters.", answer = "Fact", id=8),
            SecondGame(text = "Reading books is the best way to gain knowledge.", answer = "Opinion", id=9)
        )

        questions.forEach { question ->
            database.child("games").child("secondGame").child(question.id.toString()).setValue(question)
        }
    }

}