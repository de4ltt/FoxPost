
package com.transport.ui.algorythm

import android.util.Log
import com.transport.model.CTile
import com.transport.model.Matrix
import kotlin.math.min

val matrix = Matrix(
    a = listOf(4, 6, 10, 10),
    b = listOf(7, 7, 7, 7, 2),
    c = listOf(
        listOf(
            CTile(x = 3, c = 16),
            CTile(x = 0, c = 30),
            CTile(x = 1, c = 17),
            CTile(x = 0, c = 10),
            CTile(x = 0, c = 16)
        ),
        listOf(
            CTile(x = 0, c = 30),
            CTile(x = 0, c = 27),
            CTile(x = 6, c = 26),
            CTile(x = 0, c = 9),
            CTile(x = 0, c = 23)
        ),
        listOf(
            CTile(x = 1, c = 13),
            CTile(x = 0, c = 4),
            CTile(x = 0, c = 22),
            CTile(x = 7, c = 3),
            CTile(x = 2, c = 1)
        ),
        listOf(
            CTile(x = 3, c = 3),
            CTile(x = 7, c = 1),
            CTile(x = 0, c = 5),
            CTile(x = 0, c = 4),
            CTile(x = 0, c = 24)
        )
    )
)

fun Matrix.printC(): String = this.c.joinToString(separator = "\n") { row ->
    row.joinToString(" ")
}

fun launchMethod() {

    Log.d("KARAMBA", "launched")

    val x = PotentialsMethod(matrix)

    x.first.forEach {
        Log.d("MATRICE", it.second.printC())
    }
}

private data class Element(
    val value: Int = Int.MAX_VALUE,
    val indexA: Int = Int.MAX_VALUE,
    val indexB: Int = Int.MAX_VALUE
)

private class TreeNode(val value: Element = Element()) {
    var previousNode: TreeNode? = null
    val nodes: MutableList<TreeNode> = mutableListOf()

    fun add(node: TreeNode) = nodes.add(node)
    fun addAncestor(node: TreeNode) {
        previousNode = node
    }
}

fun PotentialsMethod(
    matrix: Matrix
): Pair<List<Pair<String, Matrix>>, Int> {

    var description: String
    val solutionSteps: MutableList<Pair<String, Matrix>> = mutableListOf()

    var resMatrix = matrix

    description = "Начальная матрица"
    solutionSteps.add(Pair(description, resMatrix))

    description = "Найдем U и V"
    var uAndV = findUAndV(matrix)

    resMatrix = resMatrix.copy(u = uAndV.first, v = uAndV.second)

    solutionSteps.add(Pair(description, matrix))

    description = "Найдем оценки для свободных клеток"
    resMatrix = evaluateEvaluationsForFreeCells(resMatrix)

    var lowestEvaluatedCell = findLowestEvaluatedCell(resMatrix)
    Log.d("BLACK_MAMBA", "LOWEST FUCKING ELEMENT: $lowestEvaluatedCell")

    while (lowestEvaluatedCell.value < 0) {

        val root = TreeNode(lowestEvaluatedCell)
        root.addAncestor(root)

        val path: MutableList<Element> = mutableListOf()

        fun findAllConnectedNodes(node: TreeNode) {
            resMatrix.c[node.value.indexA].forEachIndexed { indexB, cTile ->
                cTile.evaluation?.let {
                    if (cTile.evaluation != 0)
                        node.add(TreeNode(
                            Element(
                                value = cTile.evaluation,
                                indexA = node.value.indexA,
                                indexB = indexB
                            )
                                .also {
                                    if (it == root.value) { // Проверяем сначала отличные от этого, если не нашлось =, то этот
                                        path.add(node.value)

                                        var previousNode = node.previousNode
                                        previousNode?.let { pNode ->
                                            while (previousNode != root) {
                                                path.add(pNode.value)
                                                previousNode = pNode.previousNode
                                            }
                                        }
                                        return
                                    }
                                }
                        ).also { it.addAncestor(TreeNode(node.value)) })
                }
            }

            for (i in 0 until resMatrix.a.size) {
                resMatrix.c[i][node.value.indexB].run {
                    this.evaluation?.let {
                        if (this.evaluation != 0)
                            node.add(TreeNode(
                                Element(
                                    value = this.evaluation,
                                    indexA = i,
                                    indexB = node.value.indexB
                                )
                                    .also {
                                        if (it == root.value) {
                                            path.add(node.value)

                                            var previousNode = node.previousNode

                                            previousNode?.let { pNode ->
                                                while (pNode != root) {
                                                    path.add(pNode.value)
                                                    previousNode = pNode.previousNode
                                                }
                                            }


                                            return
                                        }
                                    }
                            ).also { it.addAncestor(TreeNode(node.value)) })
                    }
                }
            }

            root.nodes.forEach { _node ->
                findAllConnectedNodes(_node)
            }
        }

        findAllConnectedNodes(root)

        var alternatingSign = -1

        var teta = Int.MAX_VALUE

        Log.d("KARAMBA", "${path.size}}")

        path.forEach { element ->
            resMatrix = resMatrix.copy(
                c = resMatrix.c.mapIndexed { indexAmap, row ->
                    row.mapIndexed { indexBmap, cTile ->
                        if (indexAmap == element.indexA && indexBmap == element.indexB)
                            cTile.copy(res = alternatingSign)
                                .also {
                                    if (alternatingSign < 0)
                                        cTile.x?.let { x ->
                                            teta = min(x, teta)
                                        }

                                }
                        else cTile
                    }
                }
            )
            alternatingSign *= -1
        }

        description =
            "Находим клетку с минимальной оценкой, строим цикл с началом в этой клетке, помечая чередуя \"-\" и \"+\" каждую клетку. Среди клеток, отмеченных знаком минус находим ту, что имеет минимальное значение X."
        solutionSteps.add(Pair(description, resMatrix))

        Log.d("ROOT", "112312 ${path.joinToString("\n")}")

        path.forEach { element ->
            resMatrix = resMatrix.copy(
                c = resMatrix.c.mapIndexed { indexAmap, row ->
                    row.mapIndexed { indexBmap, cTile ->
                        if (indexAmap == element.indexA && indexBmap == element.indexB)
                            cTile.x?.let { x ->
                                Log.d("PATH_NULL", "$x ${cTile.res} $teta $alternatingSign")
                                cTile.copy(x = x + (cTile.res ?: 0) * teta, d = teta * alternatingSign)
                            } ?: cTile
                        else cTile
                    }
                }
            )
        }

        description =
            "Перерасчитываем перевозки в клетках на величину найденного минимального значения, учитывая знак \"-\" и \"+\""
        solutionSteps.add(Pair(description, resMatrix))

        uAndV = findUAndV(matrix)

        resMatrix = resMatrix.copy(u = uAndV.first, v = uAndV.second)

        description = "Найдем оценки для свободных клеток"
        resMatrix = resMatrix.copy(
            c = evaluateEvaluationsForFreeCells(resMatrix).c
        )

        Log.d("KARAMBA", "$uAndV\n${resMatrix.printC()}")
        solutionSteps.add(Pair(description, resMatrix))

        lowestEvaluatedCell = findLowestEvaluatedCell(resMatrix)
        Log.d("KARAMBA", "$lowestEvaluatedCell")
    }

    Log.d("KARAMBA", "1")

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

private fun findLowestEvaluatedCell(
    matrix: Matrix
): Element {
    var min = Element()

    matrix.c.forEachIndexed { indexA, row ->
        row.forEachIndexed { indexB, cTile ->
            cTile.evaluation.let {
                if (it <= min.value)
                    min = min.copy(value = it, indexA = indexA, indexB = indexB)
            }
        }
    }

    return min
}


private fun evaluateEvaluationsForFreeCells(
    matrix: Matrix
): Matrix {

    var innerMatrix = matrix

    innerMatrix.u.forEachIndexed { indexU, u ->
        innerMatrix.v.forEachIndexed { indexV, v ->

            if (innerMatrix.c[indexU][indexV].x == 0 || innerMatrix.c[indexU][indexV].x == null)
                innerMatrix = innerMatrix.copy(
                    c = innerMatrix.c.mapIndexed { indexAmap, row ->
                        row.mapIndexed { indexBmap, cTile ->
                            if (indexAmap == indexU && indexBmap == indexV)
                                cTile.copy(evaluation = (cTile.c ?: 0) - (u ?: 0) - (v ?: 0))
                                    .also {
                                        Log.d(
                                            "BLACK_MAMBA",
                                            "x = ${innerMatrix.c[indexU][indexV].x}\nc = ${innerMatrix.c[indexU][indexV].c}, u = $u, v = $v\nindexA = $indexU, indexB = $indexV\nevaluation = ${(cTile.c ?: 0) - (u ?: 0) - (v ?: 0)}"
                                        )
                                    }
                            else cTile
                        }
                    }
                )
        }
    }

    return innerMatrix
}

private fun findUAndV(
    matrix: Matrix
): Pair<List<Int?>, List<Int?>> {

    val u: MutableList<Int?> = MutableList(matrix.a.size) { null }
    val v: MutableList<Int?> = MutableList(matrix.b.size) { null }

    val unfounded: MutableList<Pair<Int, Int>> = mutableListOf()

    var isFirstIteration = true

    for (i in 0 until matrix.a.size) {
        for (j in 0 until matrix.b.size) {

            matrix.c[i][j].x?.let { x ->
                matrix.c[i][j].c?.let { c ->

                    if (x != 0) {
                        if (isFirstIteration) {

                            v[j] = c
                            u[i] = 0
                        } else {
                            u[i]?.let { u ->
                                v[j] = c - u
                            }
                            v[j]?.let { v ->
                                u[i] = c - v
                            }

                            if (u[i] == null && v[j] == null)
                                unfounded.add(Pair(i, j))
                        }
                    }
                }
            }
        }
        isFirstIteration = false
    }

    while (unfounded.isNotEmpty()) {

        unfounded.forEach { pair ->

            val i = pair.first
            val j = pair.second

            matrix.c[i][j].c?.let { c ->

                u[i]?.let { u ->
                    v[j] = c - u
                }
                v[j]?.let { v ->
                    u[i] = c - v
                }

                if (u[i] != null && v[j] != null)
                    unfounded.remove(Pair(i, j))
            }

        }
    }

    return Pair(u, v)
}
