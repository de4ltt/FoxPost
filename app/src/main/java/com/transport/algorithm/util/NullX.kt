package com.transport.algorithm.util

import com.transport.model.Matrix

val Matrix?.withNullX
    get() = this?.copy(
        c = this.c.map { row ->
            row.map {
                it.copy(
                    x = if (it.x == 0) null else it.x,
                    check = Pair(false, false)
                )
            }
        }
    )

val MatrixC.withNullX
    get() = this.map { row ->
        row.map { it.copy(x = if (it.x == 0) null else it.x) }
    }