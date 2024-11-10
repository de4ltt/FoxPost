package com.transport.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Matrix(
    val tileSize: Dp = 0.dp,
    val a: List<Int?> = listOf(null),
    val b: List<Int?> = listOf(null),
    val c: List<List<CTile>> = listOf(listOf(CTile()))
)