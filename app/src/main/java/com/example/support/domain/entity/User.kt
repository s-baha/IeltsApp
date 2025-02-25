package com.example.support.domain.entity

// must be empty  for Firebase
data class User(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val score: Int = 0,
    val rank: Int = 0
)



