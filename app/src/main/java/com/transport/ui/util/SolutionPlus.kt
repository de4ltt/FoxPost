package com.transport.ui.util

import com.transport.model.Matrix

operator fun Pair<List<Pair<String, Matrix>>, Int>.plus(
    other: Pair<List<Pair<String, Matrix>>, Int>
): Pair<List<Pair<String, Matrix>>, Pair<Int, Int>> = Pair(
    first = this.first + other.first,
    second = Pair(this.second, other.second)
)
