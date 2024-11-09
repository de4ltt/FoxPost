package com.transport.ui.util

import kotlin.math.round

internal fun calculateTileSize(
    width: Int,
    spacing: Float,
    tileQuantity: Int = 4
): Float = round(width / tileQuantity - spacing)