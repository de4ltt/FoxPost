package com.transport.algorithm

import android.util.Log
import com.transport.model.Matrix
import kotlin.math.min

private data class Element(
    val value: Int = Int.MAX_VALUE,
    val indexA: Int = Int.MAX_VALUE,
    val indexB: Int = Int.MAX_VALUE
)

private data class TreeNode(var value: Element = Element()) {
    var previousNode: TreeNode? = null
    val children: MutableList<TreeNode> = mutableListOf()

    fun born(node: TreeNode) = children.add(node)
    fun addAncestor(node: TreeNode) {
        previousNode = node
    }
}

fun calculateSolutionPotentialsMethod(
    matrix: Matrix
): List<Pair<String, Matrix?>> {

    var description: String
    val solutionSteps: MutableList<Pair<String, Matrix>> = mutableListOf()

    var resMatrix = matrix

    description = "Начальная матрица"
    solutionSteps.add(Pair(description, resMatrix))

    Log.d("FIRST_DEATH", "UV BEFORE")

    description = "Найдем U и V"
    var uAndV = findUAndV(resMatrix)

    Log.d("FIRST_DEATH", "UV PASSED")

    resMatrix = resMatrix.copy(u = uAndV.first, v = uAndV.second)

    solutionSteps.add(Pair(description, resMatrix))

    description = "Найдем оценки для свободных клеток"
    resMatrix = evaluateEvaluationsForFreeCells(resMatrix)

    Log.d("FIRST_DEATH", "EVALUATIONS PASSED")

    solutionSteps.add(Pair(description, resMatrix))

    var lowestEvaluatedCell = findLowestEvaluatedCell(resMatrix)

    Log.d("FIRST_DEATH", "LOWEST PASSED")

    var root = TreeNode(lowestEvaluatedCell)

    val path = mutableListOf(listOf<TreeNode>())

    while (lowestEvaluatedCell.value < 0) {

        Log.d("ALL", "LOWEST ===== ${lowestEvaluatedCell}")

        fun findAllChildren(node: TreeNode, visited: MutableSet<Element> = mutableSetOf()) {

            if (node.value != root.value)
                visited.add(node.value)

            resMatrix.c.forEachIndexed { indexA, row ->
                row.forEachIndexed { indexB, cTile ->

                    val isElementOnTheSameCrossAndNotEqual = (indexA == node.value.indexA || indexB == node.value.indexB)
                            && !(indexA == node.value.indexA && indexB == node.value.indexB)

                    if (isElementOnTheSameCrossAndNotEqual) {

                        val newElement = Element(
                            value = cTile.evaluation,
                            indexA = indexA,
                            indexB = indexB
                        )

                        if (newElement !in visited) {
                            val newNode = TreeNode(newElement)

                            if (cTile.x != 0 || newElement == root.value) {
                                newNode.addAncestor(node)
                                node.born(newNode)
                            }
                        }
                    }
                }
            }

            var flag = false
            node.children.forEach { _node ->
                if (_node.value == root.value) {

                    _node.addAncestor(node)

                    val cycle = makePath(node)

                    fun checkList(): Boolean {
                        val indexesASet = cycle.map { it.value.indexA }.toSet()
                        val indexesBSet = cycle.map { it.value.indexB }.toSet()

                        var evenInRow = true
                        indexesASet.forEach { aSet ->
                            val li = cycle.filter { aSet == it.value.indexA }
                            evenInRow = evenInRow && li.size % 2 == 0
                        }

                        var evenInColumn = true
                        indexesBSet.forEach { bSet ->
                            val li = cycle.filter { bSet == it.value.indexB }
                            evenInColumn = evenInColumn && li.size % 2 == 0
                        }

                        return evenInRow && evenInColumn
                    }

                    if (cycle.count { it.value == root.value } == 1 && checkList() && cycle.isNotEmpty())
                        path.add(cycle)

                    flag = true
                }
            }

            node.children.forEach { _node ->
                if (_node.value !in visited) {
                    findAllChildren(_node, visited.toMutableSet())
                }
            }

            if (flag)
                return
        }

        findAllChildren(root)

        var minX = Int.MAX_VALUE


        Log.d("PATHS", path[0].joinToString("\n"))


        path[0].dropLast(1).forEach { node ->
            minX = min(resMatrix.c[node.value.indexA][node.value.indexB].x ?: 0, minX)
        }

        var alt = -1

        path[0].reversed().forEach { node ->

            resMatrix = resMatrix.copy(
                c = resMatrix.c.mapIndexed { indexA, row ->
                    if (node.value.indexA == indexA)
                        row.mapIndexed { indexB, cTile ->
                            if (node.value.indexB == indexB) {
                                alt *= -1
                                cTile.copy(d = alt * minX, evaluation = 0)
                            }
                            else cTile
                        }
                    else row
                }
            )
        }

        path[0].reversed().forEach { node ->
            resMatrix = resMatrix.copy(
                c = resMatrix.c.mapIndexed { indexA, row ->
                    row.mapIndexed { indexB, cTile ->
                        cTile.x?.let { x ->
                            if (indexA == node.value.indexA && indexB == node.value.indexB)
                                cTile.copy(x = x + (cTile.d ?: 0))
                            else cTile
                        } ?: cTile
                    }
                }
            )
        }


        resMatrix = resMatrix.copy(
            c = resMatrix.c.mapIndexed { indexA, row ->
                    row.mapIndexed { indexB, cTile ->
                        cTile.copy(d = 0, evaluation = 0)
                    }
            }
        )


        Log.d("FIND", "UV STARTED")
        description = "Найдем U и V"
        uAndV = findUAndV(resMatrix)
        Log.d("FIND", "UV ENDED")

        resMatrix = resMatrix.copy(u = uAndV.first, v = uAndV.second)

        solutionSteps.add(Pair(description, resMatrix))

        description = "Найдем оценки для свободных клеток"
        resMatrix = evaluateEvaluationsForFreeCells(resMatrix)

        solutionSteps.add(Pair(description, resMatrix))

        lowestEvaluatedCell = findLowestEvaluatedCell(resMatrix)

        root = TreeNode(lowestEvaluatedCell)

        path.clear()
    }

    description = "Общая стоимость перевозок по этому плану:"

    solutionSteps.add(Pair(description, resMatrix))

    return solutionSteps
}

private fun makePath(node: TreeNode): List<TreeNode> {

    val path = mutableListOf<TreeNode>()
    path.add(node)

    var previousNode = node.previousNode

    while (previousNode != null) {
        path.add(previousNode)

        previousNode = previousNode.previousNode
    }

    return path
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

    innerMatrix = innerMatrix.copy(
        c = innerMatrix.c.mapIndexed { indexAmap, row ->
            row.mapIndexed { indexBmap, cTile ->
                if ((cTile.x ?: 0) == 0)
                    cTile.copy(evaluation = (cTile.c ?: 0) - (matrix.u[indexAmap] ?: 0) - (matrix.v[indexBmap] ?: 0))
                else cTile
            }
        }
    )

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

                            v[j] = 0
                            u[i] = c

                            isFirstIteration = false
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
    }

    Log.d("UV", "U ===== ${u.joinToString(" ")} V ===== ${v.joinToString(" ")}")

    Log.d("FIRST_DEATH", "FOR FOR PASSED")

    while (unfounded.isNotEmpty()) {

        val iterator = unfounded.iterator()

        Log.d("FIRST_DEATH", "OUTER_WHILE")

        while (iterator.hasNext()) {
            Log.d("FIRST_DEATH", "ITERATOR STARTED")
            val pair = iterator.next()
            Log.d("FIRST_DEATH", "ITERATOR ENDED")
            val i = pair.first
            val j = pair.second

            matrix.c[i][j].c?.let { c ->
                u[i]?.let { u ->
                    v[j] = c - u
                } ?: 0
                v[j]?.let { v ->
                    u[i] = c - v
                } ?: 0
                Log.d("PAIR", "PAAAAAAAAAAAIR $pair Ui ${u[i]} Vj ${v[j]}")
                if (u[i] != null && v[j] != null) {
                    Log.d("FIRST_DEATH", "REMOVE STARTED $pair")
                    iterator.remove()
                    Log.d("FIRST_DEATH", "REMOVE ENDED")
                }
            }
        }
    }

    Log.d("FIRST_DEATH", "UV PASSED")

    Log.d("ALL", "${Pair(u, v)}")

    return Pair(u, v)
}
