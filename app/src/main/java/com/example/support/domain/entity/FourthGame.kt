package com.example.support.domain.entity

data class FourthGame(
    val category: String = "",
    val mainWord: String = "",
    val synonyms: List<String> = emptyList(),
    val otherWords: List<String> = emptyList()
)