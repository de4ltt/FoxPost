package com.transport.algorithm.util

import android.util.Log
import com.transport.model.Matrix

val Matrix?.isValid
    get(): Boolean {

    if (this == null)
        return false

    val systemSize = this.a.size + this.b.size - 1

    val xNotZero = this.c.sumOf { row ->
        row.count {
            it.x?.let { x ->
                x != 0
            } ?: false
        }
    }

    Log.d("SDOFHODSFH", "S ====== $systemSize X ====== $xNotZero")

    return systemSize == xNotZero
}