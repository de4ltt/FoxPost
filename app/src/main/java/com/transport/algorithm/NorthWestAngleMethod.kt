package com.transport.algorithm

import com.transport.model.Matrix

fun calculateSolutionNorthWestAngleMethod(
    matrix: Matrix
): List<Matrix> {

    val solutionSteps: MutableList<Matrix> = mutableListOf()

    var indexA = 0
    var indexB = 0

    var resMatrix = matrix

    while (indexA < resMatrix.a.size && indexB < resMatrix.b.size) {
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

                    solutionSteps.add(resMatrix)

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
                        resMatrix
                    )

                    if (resMatrix.b[indexB] != 0)
                        indexA++
                    else indexB++
                }
            }
        }
    }

    return solutionSteps
}