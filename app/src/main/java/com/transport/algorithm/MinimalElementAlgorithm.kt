package com.transport.algorithm

import android.util.Log
import com.transport.model.CTile
import com.transport.model.Matrix

/**
 * @param initialMatrix исходная матрица
 * @return список шагов решения в формате (описание, матрица)
 * @author Timko Aleksey
 */
fun calculateSolutionMinimalElementAlgorithm(
    initialMatrix: Matrix
): Pair<List<Pair<String, Matrix>>, Int> {

    /**
     * @param matrix изменяющаяся матрица
     */
    var matrix = initialMatrix
    val solutions = mutableListOf<Pair<String, Matrix>>()

    /**
     * @param matrix Matrix матрица для поиска
     * @return Позиция минимального элемента
     */
    fun findMinimalElem(matrix: Matrix): Pair<Int, Int> {

        /**
         * @param c матрица стоимостей
         * @param min минимальный элемент
         * @param position позиция минимального элемента
         */
        val c = matrix.c.map { it.map { item -> item.c ?: 0 } }
        var minimum = c[0][0]
        var i = 0
        var j = 0

        for (k in c.indices)
            for (m in c[k].indices) {
                if (matrix.c[k][m].x != null && c[k][m] < minimum) {
                    minimum = c[k][m]
                    i = k
                    j = m
                }
            }

        return Pair(i, j)
    }

    /**
     * @return подсчитывает сумму элементов [a] и [b]
     */
    fun calculateSumOfAB(): Int {
        Log.d("SOLUTION", "${matrix.a.sumOf { it ?: 0 } + matrix.b.sumOf { it ?: 0 }}")
        return matrix.a.sumOf { it ?: 0 } + matrix.b.sumOf { it ?: 0 }
    }


    fun reduceAB(i: Int, j: Int): Int {

        val a = matrix.a[i] ?: 0
        val b = matrix.b[j] ?: 0

        var res: Int = 0

        Log.d("SOLUTION", "a = $a, b = $b")

        if (a > b)
            matrix = matrix.copy(
                a = List(matrix.a.size) { index ->
                    if (index == i) {
                        res = a - b
                        a - b
                    }
                    else a
                }
            )
        else
            matrix = matrix.copy(
                b = List(matrix.a.size) { index ->
                    if (index == j) {
                        res = b - a
                        b - a
                    }
                    else b
                }
            )

        return res
    }

    while (calculateSumOfAB() > 0) {
        val (i, j) = findMinimalElem(matrix)

        Log.d("SOLUTION", "$i $j")

        reduceAB(i, j)

        solutions += Pair(
            first = annotateStep(i, j, matrix.c[i][j].c ?: 0),
            second = matrix
        )
    }

    val result = matrix.c.sumOf {
        it.sumOf { item ->
            (item.c ?: 0) * (item.x ?: 0)
        }
    }

    return Pair(solutions, result)
}

private fun annotateStep(i: Int, j: Int, minimum: Int) =
    "Минимальный элемент находится на позиции [$i, $j]. X[$i][$j] = min(A[$i], B[$j]) = $minimum"

val matrix = Matrix(
    a = listOf(7, 7, 7, 7, 2),
    b = listOf(4, 6, 10, 10),
    c = listOf(
        listOf(CTile(c = 16), CTile(c = 30), CTile(c = 17), CTile(c = 10), CTile(c = 16)),
        listOf(CTile(c = 30), CTile(c = 27), CTile(c = 26), CTile(c = 9), CTile(c = 23)),
        listOf(CTile(c = 13), CTile(c = 30), CTile(c = 17), CTile(c = 10), CTile(c = 16)),
        listOf(CTile(c = 3), CTile(c = 1), CTile(c = 5), CTile(c = 4), CTile(c = 24)),
    )
)


fun display() {

    val res = calculateSolutionMinimalElementAlgorithm(
        initialMatrix = matrix
    )

    res.first.forEachIndexed { i, v ->
        Log.d("MATRIX [$i]", "${v.first}\n${v.second}")
    }
}