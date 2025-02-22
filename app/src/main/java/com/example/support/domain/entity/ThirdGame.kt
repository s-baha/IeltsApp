package com.example.support.domain.entity

// must be empty  for Firebase
data class ThirdGame(
    val id: Long =0,
    val text :String="",
    val answers: List<String> = emptyList(),
)