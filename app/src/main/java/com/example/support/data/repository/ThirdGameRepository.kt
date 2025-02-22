package com.example.support.data.repository

import com.example.support.domain.entity.ThirdGame
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ThirdGameRepository @Inject constructor(
    private val database: DatabaseReference
) {

    // Get a random sentence from the Firebase
    suspend fun getRandomSentence(): ThirdGame? = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            database.child("games").child("thirdGame").get()
                .addOnSuccessListener { snapshot ->
                    val sentences = snapshot.children.mapNotNull { it.getValue(ThirdGame::class.java) }
                    val randomSentence = sentences.randomOrNull()
                    continuation.resume(randomSentence)
                }
        }
    }

    // Initialization of the database (loading example sentences)
    fun insertInitialData() {
        val sentences = listOf(
            ThirdGame(
                text = "Traveling to new countries allows people to experience different cultures. It helps broaden perspectives and develop a better understanding of the world. Many travelers enjoy trying local food and exploring historical sites.",
                answers = listOf("Traveling", "cultures"),
                id = 1
            ),
            ThirdGame(
                text = "Artificial intelligence is transforming various industries. It is used in healthcare, finance, and even creative fields. While AI increases efficiency, some fear it may replace human jobs.",
                answers = listOf("Artificial intelligence", "industries"),
                id = 2
            ),
            ThirdGame(
                text = "Exercise is essential for maintaining good health. Regular physical activity strengthens muscles and improves heart function. Many people choose activities like running, swimming, or yoga.",
                answers = listOf("Exercise","health"),
                id = 3
            ),
            ThirdGame(
                text = "Reading books is a great way to expand knowledge. It improves vocabulary and stimulates imagination. Many people find reading a relaxing escape from daily stress.",
                answers = listOf("Reading","knowledge"),
                id = 4
            ),
            ThirdGame(
                text = "Online shopping has become increasingly popular. It offers convenience and a wide range of products. However, some consumers prefer physical stores to see items before purchasing.",
                answers = listOf("Online shopping","convenience"),
                id = 5
            ),
            ThirdGame(
                text = "Wildlife conservation is important for protecting endangered species. Deforestation and pollution threaten many animals. Governments and organizations work to preserve natural habitats.",
                answers = listOf("Wildlife conservation","endangered species"),
                id = 6
            ),
            ThirdGame(
                text = "Music has a powerful impact on emotions. It can bring back memories and influence mood. Different genres appeal to different personalities and situations.",
                answers = listOf("Music","emotions"),
                id = 7
            ),
            ThirdGame(
                text = "Time management is a key skill for success. Planning tasks effectively helps reduce stress and improve productivity. Many people use calendars or apps to organize their schedules.",
                answers = listOf("Time management","productivity"),
                id = 8
            ),
            ThirdGame(
                text = "Recycling helps reduce waste and protect the environment. Sorting plastics, paper, and glass can make a big difference. Many governments encourage recycling through awareness campaigns.",
                answers = listOf("Recycling","environment"),
                id = 9
            )



        )
        sentences.forEach { sentence ->
            database.child("games").child("thirdGame").child(sentence.id.toString()).setValue(sentence)
        }
    }
}
