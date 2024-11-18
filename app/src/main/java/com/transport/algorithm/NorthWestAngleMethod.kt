package com.transport.algorithm

import com.transport.model.Matrix

fun calculateSolutionNorthWestAngleMethod(
    matrix: Matrix
): List<Pair<String, Matrix?>> {

    val solutionSteps: MutableList<Pair<String, Matrix>> = mutableListOf()

    solutionSteps.add(Pair("", matrix))

    var indexA = 0
    var indexB = 0

    val initialA = matrix.a
    val initialB = matrix.b

    var resMatrix = matrix

    while (indexA < resMatrix.a.size && indexB < resMatrix.b.size) {
        if (resMatrix.c[indexA][indexB].x == null)
            resMatrix.a[indexA]?.let { a ->
                resMatrix.b[indexB]?.let { b ->
                    if (a > b) {

                        resMatrix = resMatrix.copy(
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

                        solutionSteps.add(Pair("", resMatrix.copy(a = initialA, b = initialB)))

                        if (resMatrix.a[indexA] != 0)
                            indexB++
                        else indexA++
                    } else {

                        resMatrix = resMatrix.copy(
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

                        solutionSteps.add(
                            Pair("", resMatrix.copy(a = initialA, b = initialB))
                        )

                        if (resMatrix.b[indexB] != 0)
                            indexA++
                        else indexB++
                    }
                }
            }
        else {
            if (indexB + 1 == resMatrix.b.size - 1)
                if (indexA + 1 == resMatrix.a.size - 1)
                     return solutionSteps
                else indexA++
            else indexB++
        }
    }

    return solutionSteps
}