package com.transport.model.event

import androidx.compose.ui.unit.Dp
import com.transport.model.state.ScreenMode

sealed class MatrixUIEvent {

    data class UpdateTileSize(val tileSize: Dp): MatrixUIEvent()

    data object AddA: MatrixUIEvent()
    data object AddB: MatrixUIEvent()

    data class ChangeA(val index: Int, val newValue: Int): MatrixUIEvent()
    data class ChangeB(val index: Int, val newValue: Int): MatrixUIEvent()
    data class ChangeC(val position: Pair<Int, Int>, val newValue: Int): MatrixUIEvent()

    data class DeleteA(val index: Int): MatrixUIEvent()
    data class DeleteB(val index: Int): MatrixUIEvent()

    data class ChangeScreenMode(val screenMode: ScreenMode): MatrixUIEvent()
}