package com.transport.model.event

sealed class MatrixUIEvent {

    data object AddA: MatrixUIEvent()
    data object AddB: MatrixUIEvent()

    data class ChangeA(val index: Int, val newValue: Int): MatrixUIEvent()
    data class ChangeB(val index: Int, val newValue: Int): MatrixUIEvent()
    data class ChangeC(val position: Pair<Int, Int>, val newValue: Int): MatrixUIEvent()

    data class DeleteA(val index: Int): MatrixUIEvent()
    data class DeleteB(val index: Int): MatrixUIEvent()
}