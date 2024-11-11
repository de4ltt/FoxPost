package com.transport.model

/**
 * @param c Cij
 * @param x Xij
 * @param d изменение в соответствии с марштутом (+-)
 */
data class CTile(
    val c: Int? = null,
    val x: Int? = null,
    val d: Int? = null,
    val check: Pair<Boolean, Boolean> = Pair(false, false)
)
