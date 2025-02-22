package com.example.support.util

sealed interface Result {
    data class Success<T>(val msq:String="" , val data: T?=null): Result
    data class Failure<T>(val msq:String="" , val data: T?=null): Result

}