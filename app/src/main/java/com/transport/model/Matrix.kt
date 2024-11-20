package com.transport.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val matrix = Matrix(
    a = listOf(4, 6, 10, 10),
    b = listOf(7, 7, 7, 7, 2),
    c = listOf(
        listOf(
            CTile(x = 3, c = 16),
            CTile(x = null, c = 30),
            CTile(x = 1, c = 17),
            CTile(x = null, c = 10),
            CTile(x = null, c = 16)
        ),
        listOf(
            CTile(x = null, c = 30),
            CTile(x = null, c = 27),
            CTile(x = 6, c = 26),
            CTile(x = null, c = 9),
            CTile(x = null, c = 23)
        ),
        listOf(
            CTile(x = 1, c = 13),
            CTile(x = null, c = 4),
            CTile(x = null, c = 22),
            CTile(x = 7, c = 3),
            CTile(x = 2, c = 1)
        ),
        listOf(
            CTile(x = 3, c = 3),
            CTile(x = 7, c = 1),
            CTile(x = null, c = 5),
            CTile(x = null, c = 4),
            CTile(x = null, c = 24)
        )
    )
)

val matrix_n = Matrix(
    a = listOf(4, 6, 10, 10),
    b = listOf(7, 7, 7, 7, 2),
    c = listOf(
        listOf(
            CTile(c = 16),
            CTile(c = 30),
            CTile(c = 17),
            CTile(c = 10),
            CTile(c = 16)
        ),
        listOf(
            CTile(c = 30),
            CTile(c = 27),
            CTile(c = 26),
            CTile(c = 9),
            CTile(c = 23)
        ),
        listOf(
            CTile(c = 13),
            CTile(c = 4),
            CTile(c = 22),
            CTile(c = 3),
            CTile(c = 1)
        ),
        listOf(
            CTile(c = 3),
            CTile(c = 1),
            CTile(c = 5),
            CTile(c = 4),
            CTile(c = 24)
        )
    )
)

val matrix_Mich = Matrix(
    a = listOf(25, 11, 33, 36),
    b = listOf(18, 26, 16, 31, 14),
    c = listOf(
        listOf(
            CTile(c = 4),
            CTile(c = 9),
            CTile(c = 16),
            CTile(c = 13),
            CTile(c = 18)
        ),
        listOf(
            CTile(c = 10),
            CTile(c = 12),
            CTile(c = 13),
            CTile(c = 13),
            CTile(c = 17)
        ),
        listOf(
            CTile(c = 2),
            CTile(c = 7),
            CTile(c = 10),
            CTile(c = 15),
            CTile(c = 9)
        ),
        listOf(
            CTile(c = 1),
            CTile(c = 8),
            CTile(c = 4),
            CTile(c = 5),
            CTile(c = 8)
        )
    )
)

data class Matrix(
    val tileSize: Dp = 0.dp,
    val a: List<Int?> = listOf(null),
    val b: List<Int?> = listOf(null),
    val c: List<List<CTile>> = listOf(listOf(CTile())),
    var u: List<Int?> = listOf(null),
    var v: List<Int?> = listOf(null)
)

val mtt = Matrix(
    a = listOf(8, 24, 11, 32),
    b = listOf(15, 24, 11, 24, 1),
    c = listOf(
        listOf(
            CTile(x = null, c = 11),
            CTile(x = null, c = 7),
            CTile(x = null, c = 15),
            CTile(x = null, c = 1),
            CTile(x = null, c = 4)
        ),
        listOf(
            CTile(x = 15, c = 3),
            CTile(x = null, c = 8),
            CTile(x = 8, c = 6),
            CTile(x = null, c = 6),
            CTile(x = 1, c = 4)
        ),
        listOf(
            CTile(x = null, c = 11),
            CTile(x = 11, c = 7),
            CTile(x = null, c = 14),
            CTile(x = null, c = 6),
            CTile(x = null, c = 12)
        ),
        listOf(
            CTile(x = null, c = 8),
            CTile(x = 13, c = 9),
            CTile(x = 3, c = 14),
            CTile(x = 16, c = 4),
            CTile(x = null, c = 1)
        )
    )
)
