package com.transport.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Matrix(
    val tileSize: Dp = 0.dp,
    val a: List<Int?> = mtt.a,
    val b: List<Int?> = mtt.b,
    val c: List<List<CTile>> = mtt.c,
    var u: List<Int?> = listOf(null),
    var v: List<Int?> = listOf(null)
)

val mtt = Matrix(
    a = listOf(8, 24, 11, 32),
    b = listOf(15, 24, 11, 24, 1),
    c = listOf(
        listOf(
            CTile(x = 0, c = 11),
            CTile(x = 0, c = 7),
            CTile(x = 0, c = 15),
            CTile(x = 8, c = 1),
            CTile(x = 0, c = 4)
        ),
        listOf(
            CTile(x = 15, c = 3),
            CTile(x = 0, c = 8),
            CTile(x = 8, c = 6),
            CTile(x = 0, c = 6),
            CTile(x = 1, c = 4)
        ),
        listOf(
            CTile(x = 0, c = 11),
            CTile(x = 11, c = 7),
            CTile(x = 0, c = 14),
            CTile(x = 0, c = 6),
            CTile(x = 0, c = 12)
        ),
        listOf(
            CTile(x = 0, c = 8),
            CTile(x = 13, c = 9),
            CTile(x = 3, c = 14),
            CTile(x = 16, c = 4),
            CTile(x = 0, c = 1)
        )
    )
)
