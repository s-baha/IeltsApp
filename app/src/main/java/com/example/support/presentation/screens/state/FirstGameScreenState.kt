package com.example.support.presentation.screens.state

sealed class FirstGameScreenEvent {
    data class Text1(val text1: String)
    data class Text2(val text2: String)
    data class Answer(val answer: String)
}

data class FirstGameScreenState(
    val text1:String= "",
    val text2:String= "",
    val answer:String= ""
)
