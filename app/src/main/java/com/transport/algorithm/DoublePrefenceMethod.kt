package com.transport.ui.algorythm

import com.transport.model.Matrix
import kotlin.math.min

fun calculateSolutionDoublePreferenceMethod(
    matrix: Matrix
): Pair<List<Pair<String, Matrix>>, Int> {

    val solutionSteps: MutableList<Pair<String, Matrix>> = mutableListOf()

    var resMatrix = matrix

    val initialDescription = "Начальная матрица"
    solutionSteps.add(Pair(initialDescription, resMatrix))

    /** Находим минимумы в строках */
    resMatrix.a.forEachIndexed { indexA, _ ->
        val minElement = resMatrix.c[indexA].minBy { it.c ?: Int.MAX_VALUE }

        resMatrix = resMatrix.copy(
            c = resMatrix.c.map { row ->
                row.map {
                    if (it == minElement)
                        it.copy(check = it.check.copy(first = true))
                    else it
                }
            }
        )
    }

    var stepDescription = "Находим минимумы на каждой строке"
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

    stepDescription = "Находим минимумы в каждом столбце"
    solutionSteps.add(Pair(stepDescription, resMatrix))

    var indexA = 0
    var indexB = 0

    /** Находим x для клеток с 2-мя галочками */
    while (indexA < resMatrix.a.size && indexB < resMatrix.b.size) {

        resMatrix.a[indexA]?.let { a ->
            resMatrix.b[indexB]?.let { b ->

                if (resMatrix.c[indexA][indexB].check != Pair(true, true)) {
                    if (indexB == resMatrix.b.size - 1) {
                        indexA++
                        indexB = 0
                    } else indexB++
                }
                else {
                    if (a < b) {
                        resMatrix = resMatrix.addZeroes(
                            isForRow = true,
                            indexA = indexA,
                            indexB = indexB,
                            value = a
                        )
                        stepDescription = "Устанавливаем X[$indexA][$indexB] для элемента дважды отмеченного минимальным = min([$indexA], [$indexB]) = $a"
                    }
                    else {
                        resMatrix = resMatrix.addZeroes(
                            isForRow = true,
                            indexA = indexA,
                            indexB = indexB,
                            value = b
                        )
                        stepDescription = "Устанавливаем X[$indexA][$indexB] для элемента дважды отмеченного минимальным = min([$indexA], [$indexB]) = $b"
                    }

                    if (indexB == resMatrix.b.size - 1) {
                        indexA++
                        indexB = 0
                    } else indexB++

                    solutionSteps.add(Pair(stepDescription, resMatrix))
                }
            }
        }
    }

    indexA = 0
    indexB = 0

    /** Находим x для клеток с 1-ой галочкой */
    while (indexA < resMatrix.a.size && indexB < resMatrix.b.size) {

        resMatrix.a[indexA]?.let { a ->
            resMatrix.b[indexB]?.let { b ->

                if (resMatrix.c[indexA][indexB].check != Pair(true, false) || resMatrix.c[indexA][indexB].x == 0) {
                    if (indexB == resMatrix.b.size - 1) {
                        indexA++
                        indexB = 0
                    } else indexB++
                }
                else {

                    var rowNum = a
                    var columnNum = b

                    for (i in 0 until indexB)
                        rowNum -= resMatrix.c[indexA][i].x ?: 0

                    for (i in 0 until indexA)
                        columnNum -= resMatrix.c[i][indexB].x ?: 0

                    if (rowNum < columnNum) {
                        resMatrix = resMatrix.addZeroes(
                            isForRow = true,
                            indexA = indexA,
                            indexB = indexB,
                            value = rowNum
                        )
                        stepDescription = "Устанавливаем X[$indexA][$indexB] для элемента единожды отмеченного минимальным = min([$indexA] минус сумма элементов по строке до него, [$indexB] минус сумма элементов по столбцу до него) = $rowNum"
                    }
                    else {
                        resMatrix = resMatrix.addZeroes(
                            isForRow = false,
                            indexA = indexA,
                            indexB = indexB,
                            value = columnNum
                        )
                        stepDescription = "Устанавливаем X[$indexA][$indexB] для элемента единожды отмеченного минимальным = min([$indexA] минус сумма элементов по строке до него, [$indexB] минус сумма элементов по столбцу до него) = $columnNum"
                    }

                    if (indexB == resMatrix.b.size - 1) {
                        indexA++
                        indexB = 0
                    } else indexB++

                    solutionSteps.add(Pair(stepDescription, resMatrix))
                }

            }
        }
    }

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

        if (minElement.value != Int.MAX_VALUE) {
            var rowNum = resMatrix.a[minElement.a] ?: Int.MAX_VALUE
            var columnNum = resMatrix.b[minElement.b] ?: Int.MAX_VALUE

            for (i in 0 until resMatrix.b.size)
                rowNum -= resMatrix.c[minElement.a][i].x ?: 0

            for (i in 0 until resMatrix.a.size)
                columnNum -= resMatrix.c[i][minElement.b].x ?: 0


            if (rowNum < columnNum) {
                resMatrix = resMatrix.addZeroes(
                    isForRow = true,
                    indexA = minElement.a,
                    indexB = minElement.b,
                    value = rowNum
                )
                stepDescription = "Устанавливаем X[$indexA][$indexB] для оставшихся незаполненных и неотмеченных ячеек = min([$indexA] минус сумма элементов по строке до него, [$indexB] минус сумма элементов по столбцу до него) = $rowNum"
            }
            else {
                resMatrix = resMatrix.addZeroes(
                    isForRow = false,
                    indexA = minElement.a,
                    indexB = minElement.b,
                    value = columnNum
                )
                stepDescription = "Устанавливаем X[$indexA][$indexB] для оставшихся незаполненных и неотмеченных ячеек = min([$indexA] минус сумма элементов по строке до него, [$indexB] минус сумма элементов по столбцу до него) = $rowNum"
            }

            solutionSteps.add(Pair(stepDescription, resMatrix))
        }

    } while (doesNonValuedElementExist)

    var sum = 0

    resMatrix.c.forEach { row ->
        row.forEach { cTile ->
            cTile.x?.let { x ->
                cTile.c?.let { c ->
                    sum += x * c
                }
            }
        }
    }

    return Pair(solutionSteps, sum)
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