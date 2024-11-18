package com.transport.algorithm

import com.transport.model.CTile
import com.transport.model.Matrix

val Matrix.equalized
    get(): Matrix {

        val sumA = this.a.sumOf { (it ?: 0) }
        val sumB = this.b.sumOf { (it ?: 0) }

        val newA: MutableList<Int?> = this.a.toMutableList()
        val newB: MutableList<Int?> = this.b.toMutableList()
        var newC: List<List<CTile>> = this.c

        if (sumA > sumB) {
            newB.add(sumA - sumB)
            newC = newC.addCTileColumn()
        } else if (sumB > sumA) {
            newA.add(sumB - sumA)
            newC = newC.addCTileRow()
        }

        return this.copy(
            a = newA,
            b = newB,
            c = newC
        )
    }

fun List<List<CTile>>.addCTileRow(): List<List<CTile>> {

    val list: List<CTile> = List(this.first().size) { CTile(x = null, c = 0) }
    val newC = this.toMutableList()
    newC.add(list)

    return newC
}

private fun List<List<CTile>>.addCTileColumn() = this.map { it + listOf(CTile(x = null, c = 0)) }