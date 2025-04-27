package com.example.support.data.repository


import com.example.support.domain.entity.FourthGame
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FourthGameRepository @Inject constructor(
    private val database: DatabaseReference
) {
    suspend fun getRandomWordData(): FourthGame? = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            database.child("games").child("fourthGame").get()
                .addOnSuccessListener { snapshot ->
                    val words = snapshot.children.mapNotNull { it.getValue(FourthGame::class.java) }
                    val randomWord = words.randomOrNull()
                    continuation.resume(randomWord)
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
    }

    fun insertInitialData() {
        val words = listOf(
            FourthGame(
                category = "Environment",
                mainWord = "Pollutants",
                synonyms = listOf("contaminants", "toxins", "poisons"),
                otherWords = listOf("aggressors", "erosion", "deforestation")
            ),
            FourthGame(
                category = "Environment",
                mainWord = "Endangered",
                synonyms = listOf("at risk", "vulnerable", "threatened"),
                otherWords = listOf("rebellious", "outdated", "fortunate")
            ),
            FourthGame(
                category = "Environment",
                mainWord = "Deforestation",
                synonyms = listOf("logging", "clear-cutting", "forest loss"),
                otherWords = listOf("architecture", "irrigation", "atmosphere")
            ),
            FourthGame(
                category = "Environment",
                mainWord = "Renewable",
                synonyms = listOf("sustainable", "inexhaustible", "replenishable"),
                otherWords = listOf("exhausted", "finite", "irrelevant")
            ),
            FourthGame(
                category = "Society",
                mainWord = "Inequality",
                synonyms = listOf("disparity", "imbalance", "injustice"),
                otherWords = listOf("routine", "innovation", "location")
            ),
            FourthGame(
                category = "Society",
                mainWord = "Welfare",
                synonyms = listOf("well-being", "aid", "social security"),
                otherWords = listOf("dictatorship", "oppression", "negligence")
            ),
            FourthGame(
                category = "Society",
                mainWord = "Discrimination",
                synonyms = listOf("prejudice", "bias", "exclusion"),
                otherWords = listOf("combination", "inclusion", "expansion")
            ),
            FourthGame(
                category = "Society",
                mainWord = "Integration",
                synonyms = listOf("inclusion", "incorporation", "assimilation"),
                otherWords = listOf("separation", "division", "extinction")
            ),
            FourthGame(
                category = "Education",
                mainWord = "Curriculum",
                synonyms = listOf("syllabus", "coursework", "study plan"),
                otherWords = listOf("legislation", "medicine", "preference")
            ),
            FourthGame(
                category = "Education",
                mainWord = "Literacy",
                synonyms = listOf("reading skills", "proficiency", "fluency"),
                otherWords = listOf("sculpture", "geography", "chemistry")
            ),
            FourthGame(
                category = "Education",
                mainWord = "Pedagogy",
                synonyms = listOf("teaching methods", "instruction", "educational theory"),
                otherWords = listOf("rebellion", "competition", "regulation")
            ),
            FourthGame(
                category = "Education",
                mainWord = "Assessment",
                synonyms = listOf("evaluation", "appraisal", "grading"),
                otherWords = listOf("ignorance", "stagnation", "vacation")
            )
            // добавляешь ещё сколько хочешь
        )

        words.forEachIndexed { index, FourthGame ->
            database.child("games").child("fourthGame").child(index.toString()).setValue(FourthGame)
        }
    }
}
