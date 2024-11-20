package com.transport.algorithm

import com.transport.model.Matrix
import com.transport.ui.util.minifyDigits

fun calculateSolutionNorthWestAngleMethod(
    matrix: Matrix
): List<Pair<String, Matrix?>> {

    val solutionSteps: MutableList<Pair<String, Matrix?>> = mutableListOf()

    var description = "Начальная матрица:"
    solutionSteps.add(Pair(description, matrix))

    description = """
        Метод северо-западного угла — это простой способ получения начального допустимого решения транспортной задачи. Его суть заключается в следующем:
        
        1. Начинают с верхнего левого (северо-западного) угла таблицы транспортной задачи.
        2. Выбирают минимально возможное количество груза между спросом и предложением для текущей ячейки и распределяют его.
        3. После этого уменьшают спрос или предложение в зависимости от сделанного распределения, и переходят к следующей ячейке по строке или столбцу, пока не будут удовлетворены все ограничения.
    """.trimIndent()

    var indexA = 0
    var indexB = 0

    val initialA = matrix.a
    val initialB = matrix.b

    var resMatrix = matrix

    while (indexA < resMatrix.a.size && indexB < resMatrix.b.size) {
            resMatrix.a[indexA]?.let { a ->
                resMatrix.b[indexB]?.let { b ->
                    if (a > b) {

                        val matrixToForeseeX = resMatrix.copy(
                            a = resMatrix.a.mapIndexed { index, value -> if (index == indexA) a - b else value },
                            b = resMatrix.b.mapIndexed { index, value -> if (index == indexB) 0 else value },
                            c = resMatrix.c.mapIndexed { indexAmap, cTiles ->
                                cTiles.mapIndexed { indexBmap, cTile ->
                                    if (indexA == indexAmap && indexB == indexBmap) {
                                        cTile.copy(x = b)
                                    } else if (indexA == indexAmap && cTile.x == null)
                                        cTile.copy(x = 0)
                                    else cTile
                                }
                            }
                        )

                        val xName = "X${((indexA + 1) * 10 + indexB + 1).minifyDigits()}"
                        description = "Найдём $xName как минимум из разностей элементов по строке и по колонке, соответствующих данной ячейке\n$xName = ${matrixToForeseeX.c[indexA][indexB].x}"

                        resMatrix = matrixToForeseeX

                        solutionSteps.add(Pair(description, resMatrix.copy(a = initialA, b = initialB)))

                        if (resMatrix.a[indexA] != 0)
                            indexB++
                        else indexA++
                    } else {

                        val matrixToForeseeX = resMatrix.copy(
                            a = resMatrix.a.mapIndexed { index, value -> if (index == indexA) 0 else value },
                            b = resMatrix.b.mapIndexed { index, value -> if (index == indexB) b - a else value },
                            c = resMatrix.c.mapIndexed { indexAmap, cTiles ->
                                cTiles.mapIndexed { indexBmap, cTile ->
                                    if (indexA == indexAmap && indexB == indexBmap)
                                        cTile.copy(x = a)
                                    else if (indexB == indexBmap && cTile.x == null)
                                        cTile.copy(x = 0)
                                    else cTile
                                }
                            }
                        )

                        val xName = "X${((indexA + 1) * 10 + indexB + 1).minifyDigits()}"
                        description = "Найдём $xName как минимум из разностей элементов по строке и по колонке, соответствующих данной ячейке\n$xName = ${matrixToForeseeX.c[indexA][indexB].x}"

                        resMatrix = matrixToForeseeX

                        solutionSteps.add(Pair(description, resMatrix.copy(a = initialA, b = initialB)))

                        if (resMatrix.b[indexB] != 0)
                            indexA++
                        else indexB++
                    }
                }
            }
    }

    val res = resMatrix.c.sumOf { row ->
        row.sumOf { (it.c ?: 0) * (it.x ?: 0) }
    }
    description = "Стоимость пути вычислим как сумму Xi * Cij для всех ячеек матрицы.\nТаким образом, для метода северо-западного угла, получим стоимость пути f = $res"
    solutionSteps += Pair(description, null)

    return solutionSteps
}