package com.transport.algorithm

import com.transport.algorithm.util.absolutelyMagicalBasis
import com.transport.algorithm.util.correctBasis
import com.transport.algorithm.util.isValid
import com.transport.model.CTile
import com.transport.model.Matrix
import com.transport.model.matrix
import com.transport.ui.util.minifyDigits
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
    val solutionSteps: MutableList<Pair<String, Matrix?>> = mutableListOf()

    var resMatrix = matrix

    description = """
        Метод основан на расчёте потенциалов для строк и столбцов таблицы и оценки допустимых перемещений.

        1. Вычисляют потенциалы для строк и столбцов, удовлетворяющие условию Ui + Vj = Cij, где Cij — стоимость перевозки, а Ui и Vj — потенциалы строки и столбца соответственно.
        2. Для каждой свободной ячейки рассчитывают оценку: Aij = Ui + Vj - Cij. Если все Aij >= 0 решение оптимально. Иначе выбираем ячейку с минимальной ячейкой для улучшения решения.
        3. Составляют цикл перераспределения грузов, чтобы уменьшить стоимость. Вдоль цикла пересчитывают значения, сохраняя выполнение ограничений.
        4. Повторяют процесс, пока не будет достигнута оптимальность.
    """.trimIndent()

    solutionSteps.add(Pair(description, null))

    description = "Начальная матрица"
    solutionSteps.add(Pair(description, resMatrix))

    var uAndV = findUAndV(resMatrix)
    description = "Найдем U и V.\nU = ${uAndV.first}, V = ${uAndV.second}"

    resMatrix = resMatrix.copy(u = uAndV.first, v = uAndV.second)

    solutionSteps.add(Pair(description, resMatrix))

    description = "Найдем оценки для свободных клеток"
    resMatrix = evaluateEvaluationsForFreeCells(resMatrix)

    solutionSteps.add(Pair(description, resMatrix))

    var lowestEvaluatedCell = findLowestEvaluatedCell(resMatrix)

    var root = TreeNode(lowestEvaluatedCell)

    val path = mutableListOf(listOf<TreeNode>())

    while (lowestEvaluatedCell.value < 0) {

        fun findAllChildren(node: TreeNode, visited: MutableSet<Element> = mutableSetOf()) {

            if (node.value != root.value)
                visited.add(node.value)

            resMatrix.c.forEachIndexed { indexA, row ->
                row.forEachIndexed { indexB, cTile ->

                    val isElementOnTheSameCrossAndNotEqual =
                        (indexA == node.value.indexA || indexB == node.value.indexB)
                                && !(indexA == node.value.indexA && indexB == node.value.indexB)

                    if (isElementOnTheSameCrossAndNotEqual) {

                        val newElement = Element(
                            value = cTile.evaluation,
                            indexA = indexA,
                            indexB = indexB
                        )

                        if (newElement !in visited) {
                            val newNode = TreeNode(newElement)

                            if (cTile.x != null || newElement == root.value) {
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

        val chosenPath = path.choosePath(curMatrix = resMatrix)

        val minX = findMinXForPath(chosenPath, resMatrix.c)

        resMatrix = updateDForPath(resMatrix, chosenPath, minX)

        resMatrix = resMatrix.updateX

        resMatrix = resMatrix.makeEvaluationZero()

        val cell = chosenPath.last().value
        val magicCTile = resMatrix.c[cell.indexA][cell.indexB]
        val cellToStay = if (magicCTile.theta == 0 || MagicCells.cells.contains(magicCTile)) Pair(cell.indexA, cell.indexB) else null

        resMatrix = resMatrix.copy(c = resMatrix.c.correctBasis(cellToStay))
        resMatrix = resMatrix.copy(c = resMatrix.c.absolutelyMagicalBasis())

        description = "Тута типа аписани путье"
        solutionSteps.add(Pair(description, null))

        resMatrix = resMatrix.makeDNull()

        uAndV = findUAndV(resMatrix)

        description = "Найдем U и V.\nU = [${uAndV.first}], V = [${uAndV.second}]"

        resMatrix = resMatrix.copy(u = uAndV.first, v = uAndV.second)

        solutionSteps.add(Pair(description, resMatrix))

        description = "Найдём оценки для свободных клеток"
        resMatrix = evaluateEvaluationsForFreeCells(resMatrix)

        solutionSteps += Pair(description, resMatrix)

        lowestEvaluatedCell = findLowestEvaluatedCell(resMatrix)

        description =
            "Найдём ячейку с наименьей оценкой." +
                    "\nОна находится на позиции С${(lowestEvaluatedCell.indexA * 10 + lowestEvaluatedCell.indexB).minifyDigits()}"
        solutionSteps += Pair(description, null)

        root = TreeNode(lowestEvaluatedCell)

        path.clear()

    }

    val res = resMatrix.c.sumOf { row ->
        row.sumOf { (it.c ?: 0) * (it.x ?: 0) }
    }

    description = "Общая стоимость перевозок по этому плану f = $res"

    solutionSteps.add(Pair(description, resMatrix))

    return solutionSteps
}

private fun MutableList<List<TreeNode>>.choosePath(
    curMatrix: Matrix
): List<TreeNode> {

    var badPaths = mutableListOf<List<TreeNode>>()
    var selectedPath = this.first { it.isNotEmpty() }

    if (selectedPath.isEmpty()) return emptyList()

    fun Matrix.calcRes() = this.c.sumOf { row ->
        row.sumOf { (it.c ?: 0) * (it.x ?: 0) }
    }

    var newF = matrix.calcRes()

    var matrix = curMatrix

    fun List<TreeNode>.checkPath(m: Matrix, minPos: Triple<Int, Int, Int>): Boolean {

        val cTile = m.c[minPos.first][minPos.second]

        return if (cTile.x == null || cTile.theta == null) false
        else (cTile.x ?: 0) - cTile.theta == 0
    }

    this.filter { it.isNotEmpty() }.forEach {

        var tryMatrix = matrix

        val minX = findMinXForPath(it, tryMatrix.c)
        var minPos: Triple<Int, Int, Int> = Triple(0, 0, Int.MAX_VALUE)

        tryMatrix.c.forEachIndexed { i, row ->
            row.forEachIndexed { j, el ->
                el.x?.let { x ->
                    if (x < minPos.third)
                        minPos = Triple(i, j, x)
                }
            }
        }

        tryMatrix = updateDForPath(tryMatrix, it, minX)
        tryMatrix = tryMatrix.updateX

        if (it.checkPath(tryMatrix, minPos)) {

            val cell = selectedPath.last().value
            val cellToStay = if (tryMatrix.c[cell.indexA][cell.indexB].theta == 0) Pair(cell.indexA, cell.indexB) else null

            tryMatrix = tryMatrix.makeEvaluationZero()
            tryMatrix = tryMatrix.copy(c = tryMatrix.c.correctBasis(cellToStay).absolutelyMagicalBasis())
            tryMatrix = tryMatrix.makeDNull()

            if (tryMatrix.isValid) {
                val f = tryMatrix.calcRes()

                if (f <= newF) {
                    matrix = tryMatrix
                    newF = f
                    selectedPath = it
                }
            }
        }
    }

    return selectedPath
}

private fun findMinXForPath(path: List<TreeNode>, c: List<List<CTile>>): Int {
    var minX = Int.MAX_VALUE

    path.dropLast(1).forEachIndexed { i, node ->
        c[node.value.indexA][node.value.indexB].x?.let {
            if (i % 2 == 0)
                minX = min(it, minX)
        }
    }

    return minX
}

private val Matrix.updateX
    get(): Matrix = this.copy(
        c = this.c.map { row ->
            row.map { cTile ->
                if (cTile.x == null && cTile.theta == 0)
                    cTile.copy(x = 0)
                else if (cTile.theta != 0)
                    cTile.copy(x = (cTile.x ?: 0) + (cTile.theta ?: 0))
                else cTile
            }
        }
    )

private fun Matrix.makeEvaluationZero() =
    this.copy(
        c = this.c.map { row ->
            row.map { cTile ->
                cTile.copy(evaluation = 0)
            }
        }
    )

private fun Matrix.makeDNull() =
    this.copy(
        c = this.c.map { row ->
            row.map { cTile ->
                cTile.copy(theta = null)
            }
        }
    )

private fun updateDForPath(matrix: Matrix, path: List<TreeNode>, minX: Int): Matrix {

    var initialMatrix = matrix

    var alt = -1
    path.reversed().forEach { node ->
        alt *= -1
        initialMatrix = initialMatrix.updateD(alt, minX, node.value.indexA, node.value.indexB)
    }

    return initialMatrix
}


private fun Matrix.updateD(alt: Int, minX: Int, nodeIndexA: Int, nodeIndexB: Int) = this.copy(
    c = this.c.mapIndexed { indexA, row ->
        if (nodeIndexA == indexA)
            row.mapIndexed { indexB, cTile ->
                if (nodeIndexB == indexB) {
                    cTile.copy(theta = alt * minX, evaluation = 0)
                } else cTile
            }
        else row
    }
)

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
                if (cTile.x == null)
                    cTile.copy(
                        evaluation = (cTile.c ?: 0) - (matrix.u[indexAmap]
                            ?: 0) - (matrix.v[indexBmap] ?: 0)
                    )
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

                    if (isFirstIteration) {

                        v[j] = 0
                        u[i] = c

                        isFirstIteration = false
                    } else {
                        if (u[i] != null)
                            v[j] = c - (u[i] ?: 0)
                        else if (v[j] != null)
                            u[i] = c - (v[j] ?: 0)

                        if (u[i] == null && v[j] == null)
                            unfounded.add(Pair(i, j))
                    }
                }
            }
        }

    }

    while (unfounded.isNotEmpty()) {

        val iterator = unfounded.iterator()

        while (iterator.hasNext()) {
            val pair = iterator.next()
            val i = pair.first
            val j = pair.second

            matrix.c[i][j].c?.let { c ->

                if (u[i] != null)
                    v[j] = c - (u[i] ?: 0)
                else if (v[j] != null)
                    u[i] = c - (v[j] ?: 0)

                if (u[i] != null && v[j] != null) {
                    iterator.remove()
                }
            }
        }
    }

    return Pair(u, v)
}

object MagicCells {
    val cells: MutableSet<CTile> = mutableSetOf()
}