package com.transport.ui.util

private typealias Matrix<T> = List<List<T>>

val <T> Matrix<T>.transposed
    get(): Matrix<T> {

        val columnCount = this.maxOf { it.size }

        val newMatrix = MutableList(columnCount) { mutableListOf<T>() }

        this.forEach { row ->
            row.forEachIndexed { j, item ->
                newMatrix[j] += item
            }
        }

        return newMatrix
    }