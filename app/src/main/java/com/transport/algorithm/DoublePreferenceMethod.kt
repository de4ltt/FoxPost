package com.transport.algorithm

import android.util.Log
import com.transport.model.Matrix
import com.transport.ui.util.minifyDigits
import kotlin.math.min

fun calculateSolutionDoublePreferenceMethod(
    matrix: Matrix
): List<Pair<String, Matrix?>> {

    val solutionSteps: MutableList<Pair<String, Matrix?>> = mutableListOf()

    var resMatrix = matrix

    var initialDescription = """
        Основная идея заключается в выборе ячеек для распределения грузов на основе минимальной стоимости перевозок.

        1. Из всей таблицы находят ячейку с минимальной стоимостью перевозки.
        2. Распределяют максимально возможное количество груза в эту ячейку, удовлетворяя ограничения спроса и предложения.
        3. Исключают строку или столбец, где спрос или предложение стали равны нулю, и корректируют таблицу.
        4. Повторяют процесс до тех пор, пока все ограничения не будут выполнены.
    """.trimIndent()

    solutionSteps.add(Pair(initialDescription, null))

    initialDescription = "Начальная матрица:"
    solutionSteps.add(Pair(initialDescription, resMatrix))

    resMatrix.a.forEachIndexed { indexA, _ ->

        var minElement = Int.MAX_VALUE

        for (i in 0 until resMatrix.b.size)
            resMatrix.c[indexA][i].c?.let {
                if (it < minElement)
                    minElement = it
            }

        resMatrix = resMatrix.copy(
            c = resMatrix.c.mapIndexed { index, row ->
                row.mapIndexed { _, cTile ->
                    if (index == indexA && cTile.c == minElement)
                        cTile.copy(check = cTile.check.copy(first = true))
                    else cTile
                }
            }
        )
    }

    var stepDescription = "Находим минимальные элементы на каждой строке. Отмечаем их"
    solutionSteps.add(Pair(stepDescription, resMatrix))

    /** Находим минимумы в столбцах */
    resMatrix.b.forEachIndexed { indexB, _ ->
        var minElement = Int.MAX_VALUE

        for (i in 0 until resMatrix.a.size)
            resMatrix.c[i][indexB].c?.let {
                if (it < minElement)
                    minElement = it
            }

        resMatrix = resMatrix.copy(
            c = resMatrix.c.mapIndexed { _, row ->
                row.mapIndexed { index, cTile ->
                    if (index == indexB && cTile.c == minElement)
                        cTile.copy(check = cTile.check.copy(second = true))
                    else cTile
                }
            }
        )
    }

    stepDescription = "Находим минимальные элементы в каждом столбце. Так же отмечаем их"
    solutionSteps.add(Pair(stepDescription, resMatrix))

    var indexA = 0
    var indexB = 0

    stepDescription = "Начинаем с элементов с двойным приоритетом"
    solutionSteps.add(Pair(stepDescription, null))

    /** Находим x для клеток с 2-мя галочками */
    while (indexA < resMatrix.a.size && indexB < resMatrix.b.size) {

        resMatrix.a[indexA]?.let { a ->
            resMatrix.b[indexB]?.let { b ->

                if (resMatrix.c[indexA][indexB].check != Pair(true, true)) {
                    if (indexB == resMatrix.b.lastIndex) {
                        indexA++
                        indexB = 0
                    } else indexB++
                } else if (resMatrix.c[indexA][indexB].x == null) {

                    var rowNum = a
                    var columnNum = b

                    for (i in 0 until resMatrix.b.size)
                        if (i != indexB)
                            rowNum -= resMatrix.c[indexA][i].x ?: 0

                    for (i in 0 until resMatrix.a.size)
                        if (i != indexA)
                            columnNum -= resMatrix.c[i][indexB].x ?: 0

                    val xValue = "X${((indexA + 1) * 10 + indexB + 1).minifyDigits()}"

                    if (rowNum < columnNum) {

                        resMatrix = resMatrix.addZeroes(
                            isForRow = true,
                            indexA = indexA,
                            indexB = indexB,
                            value = rowNum
                        )
                        stepDescription =
                            "Устанавливаем $xValue для элемента дважды отмеченного минимальным = min(A${(indexA + 1).minifyDigits()}, B${(indexB + 1).minifyDigits()}) = $rowNum"
                    } else {
                        resMatrix = resMatrix.addZeroes(
                            isForRow = false,
                            indexA = indexA,
                            indexB = indexB,
                            value = columnNum
                        )
                        stepDescription =
                            "Устанавливаем $xValue для элемента дважды отмеченного минимальным = min(A${(indexA + 1).minifyDigits()}, B${(indexB + 1).minifyDigits()}) = $columnNum"
                    }

                    if (indexB == resMatrix.b.size - 1) {
                        indexA++
                        indexB = 0
                    } else indexB++

                    solutionSteps.add(Pair(stepDescription, resMatrix))
                } else {
                    if (indexB == resMatrix.b.lastIndex) {
                        indexA++
                        indexB = 0
                    } else indexB++
                }
            }
        }
    }

    indexA = 0
    indexB = 0

    stepDescription = "Тоже самое делаем для элементов с одинарным приоритетом"
    solutionSteps.add(Pair(stepDescription, null))

    while (indexA < resMatrix.a.size && indexB < resMatrix.b.size) {

        resMatrix.a[indexA]?.let { a ->
            resMatrix.b[indexB]?.let { b ->

                if (resMatrix.c[indexA][indexB].check != Pair(
                        true,
                        false
                    ) || resMatrix.c[indexA][indexB].x == 0
                ) {
                    if (indexB == resMatrix.b.size - 1) {
                        indexA++
                        indexB = 0
                    } else indexB++
                } else {

                    var rowNum = a
                    var columnNum = b

                    for (i in 0 until resMatrix.b.size)
                        if (i != indexB)
                            rowNum -= resMatrix.c[indexA][i].x ?: 0

                    for (i in 0 until resMatrix.a.size)
                        if (i != indexA)
                            columnNum -= resMatrix.c[i][indexB].x ?: 0

                    val xValue = "X${((indexA + 1) * 10 + indexB + 1).minifyDigits()}"

                    if (rowNum < columnNum) {
                        resMatrix = resMatrix.addZeroes(
                            isForRow = true,
                            indexA = indexA,
                            indexB = indexB,
                            value = rowNum
                        )
                        stepDescription =
                            "Устанавливаем $xValue для элемента единожды отмеченного минимальным = min(A${(indexA + 1).minifyDigits()} минус сумма элементов по строке до него, B${(indexB + 1).minifyDigits()} минус сумма элементов по столбцу до него) = $rowNum"
                    } else {
                        resMatrix = resMatrix.addZeroes(
                            isForRow = false,
                            indexA = indexA,
                            indexB = indexB,
                            value = columnNum
                        )
                        stepDescription =
                            "Устанавливаем $xValue для элемента единожды отмеченного минимальным = min(A${(indexA + 1).minifyDigits()} минус сумма элементов по строке до него, B${(indexB + 1).minifyDigits()} минус сумма элементов по столбцу до него) = $columnNum"                    }

                    if (indexB == resMatrix.b.size - 1) {
                        indexA++
                        indexB = 0
                    } else indexB++

                    solutionSteps.add(Pair(stepDescription, resMatrix))
                }

            }
        }
    }

    stepDescription = "Для оставшихся элементом воспользуемся методом минимального элемента"
    solutionSteps.add(Pair(stepDescription, null))

    var minElement = MinElement()

    var doesNonValuedElementExist: Boolean

    /** Находим x для клеток без галочек */
    do {

        doesNonValuedElementExist = false

        for (i in 0 until resMatrix.a.size) {
            for (j in 0 until resMatrix.b.size) {

                if (resMatrix.c[i][j].x != null)
                    continue
                else {
                    doesNonValuedElementExist = true
                    resMatrix.c[i][j].c?.let {
                        minElement = minElement.copy(min(minElement.value, it), i, j)


                    }
                }
            }
        }

        if (!doesNonValuedElementExist)
            break

        if (minElement.value != Int.MAX_VALUE) {
            var rowNum = resMatrix.a[minElement.a] ?: Int.MAX_VALUE
            var columnNum = resMatrix.b[minElement.b] ?: Int.MAX_VALUE

            for (i in 0 until resMatrix.b.size)
                if (i != indexB)
                    rowNum -= resMatrix.c[minElement.a][i].x ?: 0

            for (i in 0 until resMatrix.a.size)
                if (i != indexA)
                    columnNum -= resMatrix.c[i][minElement.b].x ?: 0

            val xValue = "X${((indexA + 1) * 10 + indexB + 1).minifyDigits()}"

            if (rowNum < columnNum) {
                resMatrix = resMatrix.addZeroes(
                    isForRow = true,
                    indexA = minElement.a,
                    indexB = minElement.b,
                    value = rowNum
                )
                stepDescription =
                    "Устанавливаем $xValue для оставшихся незаполненных и неотмеченных ячеек = min(A${(indexA + 1).minifyDigits()} минус сумма элементов по строке до него, B${(indexB + 1).minifyDigits()} минус сумма элементов по столбцу до него) = $rowNum"
            } else {
                resMatrix = resMatrix.addZeroes(
                    isForRow = false,
                    indexA = minElement.a,
                    indexB = minElement.b,
                    value = columnNum
                )
                stepDescription =
                    "Устанавливаем $xValue для оставшихся незаполненных и неотмеченных ячеек = min(A${(indexA + 1).minifyDigits()} минус сумма элементов по строке до него, B${(indexB + 1).minifyDigits()} минус сумма элементов по столбцу до него) = $columnNum"            }

            solutionSteps.add(Pair(stepDescription, resMatrix))
        }

    } while (doesNonValuedElementExist)

    val result = matrix.c.sumOf { row -> row.sumOf { tile -> (tile.c ?: 0) * (tile.x ?: 0) } }
    val description = "Стоимость пути вычислим как сумму Xi * Cij для всех ячеек матрицы.\nТаким образом, для метода минимального элемента, получим стоимость пути f = $result"
    solutionSteps += Pair(description, null)

    return solutionSteps
}


private fun Matrix.addZeroes(isForRow: Boolean, indexA: Int, indexB: Int, value: Int): Matrix {
    return this.copy(
        c = this.c.mapIndexed { indexAmap, cTiles ->
            cTiles.mapIndexed { indexBmap, cTile ->
                if (indexA == indexAmap && indexB == indexBmap) {
                    cTile.copy(x = value)
                } else if (isForRow && indexA == indexAmap && cTile.x == null)
                    cTile.copy(x = 0)
                else if (!isForRow && indexB == indexBmap && cTile.x == null)
                    cTile.copy(x = 0)
                else cTile
            }
        }
    )
}


private data class MinElement(
    val value: Int = Int.MAX_VALUE,
    val a: Int = Int.MAX_VALUE,
    val b: Int = Int.MAX_VALUE
)