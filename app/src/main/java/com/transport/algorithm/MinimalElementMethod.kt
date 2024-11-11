package com.transport.algorithm

import com.transport.model.CTile
import com.transport.model.Matrix

fun calculateSolutionMinimalElementMethod(
    initialMatrix: Matrix
): Pair<List<Pair<String, Matrix>>, Int> {

    var matrix = initialMatrix
    val solutions = mutableListOf<Pair<String, Matrix>>()

    fun findMinimalElem(matrix: Matrix): Pair<Int, Int>? {
        val c = matrix.c
        var minimum = Int.MAX_VALUE
        var minPos: Pair<Int, Int>? = null

        for (i in c.indices) {
            for (j in c[i].indices) {
                if (c[i][j].x == null && (c[i][j].c ?: 0) <= minimum) {
                    minimum = c[i][j].c ?: 0
                    minPos = Pair(i, j)
                }
            }
        }
        return minPos
    }

    fun reduceAB(i: Int, j: Int): Int {
        val a = matrix.a[i] ?: 0
        val b = matrix.b[j] ?: 0
        val amount = minOf(a, b)
        matrix = matrix.copy(
            a = matrix.a.mapIndexed { index, value -> if (index == i) (value ?: 0) - amount else value },
            b = matrix.b.mapIndexed { index, value -> if (index == j) (value ?: 0) - amount else value },
            c = matrix.c.mapIndexed { k, row ->
                row.mapIndexed { m, value ->
                    CTile(
                        d = value.d,
                        c = value.c,
                        x = if (i == k && j == m) amount else value.x
                    )
                }
            }
        )

        return amount
    }

    fun checkForCompletion(): Boolean {
        return matrix.a.all { it == 0 } && matrix.b.all { it == 0 }
    }

    val initialDescription = "Суть метода заключается в том, что мы итерационно будем выбирать элемент " +
            "с минимальной стоимостью и устанавливать максимально возможное количество доставленных товаров до тех пор," +
            " пока запасы мощностей и потребностей не иссякнут.\nНачальная матрица:"
    solutions += Pair(initialDescription, matrix.copy())

    while (!checkForCompletion()) {
        val minElemPos = findMinimalElem(matrix)

        if (minElemPos == null) break

        val (i, j) = minElemPos

        val reduce = reduceAB(i, j)

        val stepDescription =
            "Минимальный элемент находится на позиции [$i, $j]. C[$i][$j] = ${matrix.c[i][j].c}. Устанавливаем X[$i][$j] = min(A[$i], B[$j]) = $reduce"
        solutions += Pair(stepDescription, matrix.copy())
    }

    val result = matrix.c.sumOf { row -> row.sumOf { tile -> (tile.c ?: 0) * (tile.x ?: 0) } }
    return Pair(solutions, result)
}