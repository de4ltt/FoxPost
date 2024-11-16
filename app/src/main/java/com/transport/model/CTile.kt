package com.transport.model

import androidx.compose.runtime.Immutable

/**
 * @param c Cij
 * @param x Xij
 * @param evaluation оценка для свободной клетки
 * @param d изменение в соответствии с марштутом (вычет)
 */
@Immutable
data class CTile(
    val c: Int? = null,
    var x: Int? = null,
    var evaluation: Int = 0,
    val d: Int? = null,
    val res: Int? = null,
    val check: Pair<Boolean, Boolean> = Pair(false, false)
)