package com.example.support.data.repository

import android.util.Log
import com.example.support.domain.entity.User
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RatingRepository @Inject constructor(
    private val database: DatabaseReference
) {
    suspend fun getUsersByRating(): List<User> = withContext(Dispatchers.IO) {
        return@withContext try {
            val snapshot = database.child("users").orderByChild("score").get().await()
            snapshot.children.mapNotNull { child ->
                val user = child.getValue(User::class.java)
                user?.copy(id = child.key ?: "")
            }.filter { it.score != null } // Фильтруем `null`
                .sortedByDescending { it.score }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateUserRanks() {
        withContext(Dispatchers.IO) {
            try {
                val snapshot = database.child("users").get().await()
                val users = snapshot.children.mapNotNull { child ->
                    val user = child.getValue(User::class.java)
                    user?.copy(id = child.key ?: "")
                }.filter { it.score != null } // Убираем `null`
                    .sortedByDescending { it.score }

                if (users.isEmpty()) return@withContext // Защита от пустого списка

                val updates = mutableMapOf<String, Any>()
                users.forEachIndexed { index, user ->
                    val rank = index + 1
                    updates["users/${user.id}/rank"] = rank
                }

                database.updateChildren(updates).await()
                Log.d("RatingRepository", "Ranks updated successfully")
            } catch (e: Exception) {
                Log.e("RatingRepository", "Failed to update ranks: ${e.message}")
            }
        }
    }

    suspend fun getUser(userId: String): User? = withContext(Dispatchers.IO) {
        return@withContext try {
            val snapshot = database.child("users").child(userId).get().await()
            snapshot.getValue(User::class.java)?.copy(id = userId)
        } catch (e: Exception) {
            null
        }
    }
}
