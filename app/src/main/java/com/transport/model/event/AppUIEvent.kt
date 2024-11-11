package com.transport.model.event

import androidx.compose.ui.unit.Dp
import com.transport.model.state.ScreenMode

sealed class AppUIEvent {

    data class UpdateTileSize(val tileSize: Dp): AppUIEvent()

    data object AddB: AppUIEvent()
    data object AddA: AppUIEvent()

    data class ChangeB(val index: Int, val newValue: Int?): AppUIEvent()
    data class ChangeA(val index: Int, val newValue: Int?): AppUIEvent()
    data class ChangeC(val position: Pair<Int, Int>, val newValue: Int?): AppUIEvent()

    data class DeleteB(val index: Int): AppUIEvent()
    data class DeleteA(val index: Int): AppUIEvent()

    data class ChangeScreenMode(val screenMode: ScreenMode): AppUIEvent()

    data object SwitchDeleteMode: AppUIEvent()

    data object SwitchSolutionMode: AppUIEvent()
    data object FindSolution: AppUIEvent()
}