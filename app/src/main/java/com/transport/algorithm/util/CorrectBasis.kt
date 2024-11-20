package com.transport.algorithm.util

import com.transport.algorithm.MagicCells
import com.transport.model.CTile

typealias MatrixC = List<List<CTile>>

fun MatrixC.absolutelyMagicalBasis(): MatrixC =
    this.map { row ->
        row.map {
            if (MagicCells.cells.contains(it))
                it.copy(x = 0)
            else it
        }
    }

fun MatrixC.correctBasis(cellToStay: Pair<Int, Int>? = null): MatrixC {

    val isChangeRequired = this.sumOf { row ->
        row.count { it.x == 0 && it.theta != null }
    } > 1

    if (!isChangeRequired) return this.withNullX

    val basisCell = if (cellToStay != null)
        this[cellToStay.first][cellToStay.second]
    else null

    basisCell?.let {
        MagicCells.cells += it.copy(x = null, theta = null)
    }

    val remainingBasisCell = basisCell
        ?: this.map { row -> row.filter { it.x == 0 && it.theta != null } }.flatten()
            .minBy { it.c ?: 0 }

    val newMatrix = this.map { row ->
        row.map {
            if (it.x == 0)
                when (it) {
                    basisCell, remainingBasisCell -> it.copy(x = 0)
                    else -> it.copy(x = null)
                }
            else it
        }
    }

    return newMatrix
}