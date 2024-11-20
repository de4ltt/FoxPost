package com.transport.ui.util

import com.transport.model.CTile

private typealias Nodes = List<List<Triple<Int, Int, Boolean>>>
private typealias Position = Pair<Int, Int>
private typealias MatrixC = List<List<CTile>>

/**
 * @return
 */
val MatrixC.connectingNodes
    get(): List<Position> {

        val nodes: Nodes = this.mapIndexed { i, row ->
            row.mapIndexed { j, it ->
                Triple(i, j, (it.theta ?: 0) != 0)
            }
        }

        val connectingNodes = mutableListOf<Position>()

        fun connectLine(start: Position, end: Position) {

            val lineElements = if (start.first == end.first) {

                nodes[start.first].filterIndexed { index, it ->
                    index in start.second..end.second && !it.third
                }.map {
                    Pair(it.first, it.second)
                }

            } else if (start.second == end.second) {

                nodes.subList(start.first, end.first + 1).mapNotNull { row ->
                    val element = row[start.second]
                    if (!element.third) Pair(element.first, element.second) else null
                }

            } else throw IllegalArgumentException("Should be a straight line")

            connectingNodes += lineElements
        }

        fun Nodes.connectAllInRowNodes(transposed: Boolean = false) =
            this.forEachIndexed { rowIndex, row ->

                val rowNodes = row.filter { it.third }

                if (transposed)
                    rowNodes.forEachIndexed { i, item ->

                        if (item.first < rowNodes.last().first)
                            connectLine(
                                start = Pair(item.first + 1, rowIndex),
                                end = Pair(rowNodes[i + 1].first - 1, rowIndex)
                            )
                    }
                else
                    rowNodes.forEachIndexed { i, item ->

                        if (item.second < rowNodes.last().second)
                            connectLine(
                                start = Pair(rowIndex, item.second + 1),
                                end = Pair(rowIndex, rowNodes[i + 1].second - 1)
                            )
                    }
            }

        fun Nodes.connectAllInColumnNodes() =
            this.transposed.connectAllInRowNodes(transposed = true)

        nodes.connectAllInRowNodes()
        nodes.connectAllInColumnNodes()

        return connectingNodes
    }