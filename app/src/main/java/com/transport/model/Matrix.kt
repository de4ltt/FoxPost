package com.transport.model

data class Matrix(
    val a: List<Int?> = listOf(null),
    val b: List<Int?> = listOf(null),
    val c: List<List<Int?>> = listOf(listOf(null))
)