package com.transport.algorithm

import com.transport.model.CTile
import com.transport.model.Matrix
import com.transport.ui.util.minifyDigits

fun calculateSolutionMinimalElementMethod(
    initialMatrix: Matrix
): List<Pair<String, Matrix?>> {

    var matrix = initialMatrix
    val solutions = mutableListOf<Pair<String, Matrix?>>()

    val initialA = matrix.a
    val initialB = matrix.b

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
                        theta = value.theta,
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

    var initialDescription = "Суть метода заключается в том, что мы итерационно будем выбирать элемент " +
            "с минимальной стоимостью и устанавливать максимально возможное количество доставленных товаров до тех пор," +
            " пока запасы мощностей и потребностей не иссякнут."
    solutions += Pair(initialDescription, null)

    initialDescription = "Начальная матрица:"
    solutions += Pair(initialDescription, matrix.copy(a = initialA, b = initialB))

    while (!checkForCompletion()) {

        val minElemPos = findMinimalElem(matrix) ?: break

        val (i, j) = minElemPos

        val reduce = reduceAB(i, j)

        val xValue = "X${((i + 1) * 10 + j + 1).minifyDigits()}"

        val stepDescription =
            "Минимальный элемент находится на позиции C${(i * 10 + j).minifyDigits()} = ${matrix.c[i][j].c}. Устанавливаем $xValue = min(A${(i + 1).minifyDigits()}, B${(j + 1).minifyDigits()}) = $reduce"
        solutions += Pair(stepDescription, matrix.copy(a = initialA, b = initialB))
    }

    val result = matrix.c.sumOf { row -> row.sumOf { tile -> (tile.c ?: 0) * (tile.x ?: 0) } }
    val description = "Стоимость пути вычислим как сумму Xi * Cij для всех ячеек матрицы.\nТаким образом, для метода минимального элемента, получим стоимость пути f = $result"
    solutions += Pair(description, null)

    return solutions
}