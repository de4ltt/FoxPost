package com.transport.algorithm

import com.transport.model.CTile
import com.transport.model.Matrix

fun NorthWestAngleMethod(matrix: Matrix): List<Matrix> {

    val solutionSteps: MutableList<Matrix> = mutableListOf()

    var indexA = 0
    var indexB = 0

    while (indexA < matrix.a.size && indexB < matrix.b.size) {
        matrix.a[indexA]?.let { a ->
            matrix.b[indexB]?.let { b ->
                if (a > b) {
                    solutionSteps.add(
                        matrix.copy(c = matrix.c.mapIndexed { indexAmap, cTiles ->
                            cTiles.mapIndexed { indexBmap, cTile ->
                                if (indexA == indexAmap && indexB == indexBmap)
                                    cTile.copy(x = a)
                                else if (indexA == indexAmap)
                                    cTile.copy(x = 0)
                                else cTile
                            }
                        }
                        )
                    )
                    indexA++
                } else {
                    solutionSteps.add(
                        matrix.copy(c = matrix.c.mapIndexed { indexAmap, cTiles ->
                            cTiles.mapIndexed { indexBmap, cTile ->
                                if (indexA == indexAmap && indexB == indexBmap)
                                    cTile.copy(x = b)
                                else if (indexB == indexBmap)
                                    cTile.copy(x = 0)
                                else cTile
                            }
                        }
                        )
                    )
                    indexB++
                }
            }
        }
    }

    return solutionSteps
}