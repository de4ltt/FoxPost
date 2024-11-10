package com.transport.ui.util

fun Int.minifyDigits(): String {
    val subscriptDigits = mapOf(
        0 to "₀",
        1 to "₁",
        2 to "₂",
        3 to "₃",
        4 to "₄",
        5 to "₅",
        6 to "₆",
        7 to "₇",
        8 to "₈",
        9 to "₉"
    )

    return when {
        this in 0..9 -> subscriptDigits[this]!!
        this > 9 -> this.toString().map { it.digitToInt().minifyDigits() }.joinToString("")
        else -> throw IllegalArgumentException("Index is less that 0")
    }
}