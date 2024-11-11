package com.transport.ui.viewmodel

import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.transport.model.CTile
import com.transport.model.Matrix
import com.transport.model.event.AppUIEvent
import com.transport.model.state.ScreenMode
import com.transport.model.state.ScreenUIState
import com.transport.model.state.SolutionMode
import com.transport.ui.util.matchTitle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _curMatrix: MutableStateFlow<Matrix> = MutableStateFlow(Matrix())
    val curMatrix = _curMatrix.asStateFlow()

    private val _screenUIState: MutableStateFlow<ScreenUIState> = MutableStateFlow(ScreenUIState())
    val screenUIState = _screenUIState.asStateFlow()

    fun onEvent(event: AppUIEvent) = when (event) {
        AppUIEvent.AddB -> addB()
        AppUIEvent.AddA -> addA()

        is AppUIEvent.ChangeB -> changeB(event.index, event.newValue)
        is AppUIEvent.ChangeA -> changeA(event.index, event.newValue)
        is AppUIEvent.ChangeC -> changeC(event.position, event.newValue)

        is AppUIEvent.DeleteB -> deleteB(event.index)
        is AppUIEvent.DeleteA -> deleteA(event.index)

        is AppUIEvent.ChangeScreenMode -> changeScreenMode(event.screenMode)
        is AppUIEvent.UpdateTileSize -> updateTileSize(event.tileSize)

        AppUIEvent.SwitchDeleteMode -> switchDeleteMode()

        AppUIEvent.FindSolution -> findSolution()
        AppUIEvent.SwitchSolutionMode -> switchSolutionMode()
    }

    private fun updateTileSize(tileSize: Dp) = viewModelScope.launch {
        _curMatrix.value = _curMatrix.value.copy(
            tileSize = tileSize
        )
    }

    private fun addB() = viewModelScope.launch {
        _curMatrix.value = _curMatrix.value.copy(
            b = _curMatrix.value.b + null
        )

        _curMatrix.value = _curMatrix.value.copy(
            c = _curMatrix.value.c.map {
                it + CTile()
            }
        )
    }

    private fun addA() = viewModelScope.launch {
        _curMatrix.value = _curMatrix.value.copy(
            a = _curMatrix.value.a + null
        )

        _curMatrix.value = _curMatrix.value.copy(
            c = _curMatrix.value.c + listOf(List(_curMatrix.value.b.size) { CTile() })
        )
    }

    private fun changeB(index: Int, newValue: Int?) = viewModelScope.launch {
        val newB = _curMatrix.value.b.mapIndexed { i, value ->
            if (i == index) newValue
            else value
        }

        _curMatrix.value = _curMatrix.value.copy(
            b = newB
        )
    }

    private fun changeA(index: Int, newValue: Int?) = viewModelScope.launch {
        val newA = _curMatrix.value.a.mapIndexed { i, value ->
            if (i == index) newValue
            else value
        }

        _curMatrix.value = _curMatrix.value.copy(
            a = newA
        )
    }

    private fun changeC(position: Pair<Int, Int>, newValue: Int?) = viewModelScope.launch {
        val newC = _curMatrix.value.c.mapIndexed() { i, value ->
            if (i == position.first)
                value.mapIndexed { j, item ->
                    if (j == position.second)
                        item.copy(c = newValue)
                    else item
                }
            else value
        }

        _curMatrix.value = _curMatrix.value.copy(
            c = newC
        )
    }

    private fun deleteB(index: Int) = viewModelScope.launch {
        val newB = _curMatrix.value.b.filterIndexed() { i, _ -> i != index }

        val newC = _curMatrix.value.c.map { value ->
            value.filterIndexed { j, _ ->
                j != index
            }
        }

        _curMatrix.value.let {
            if (it.a.size + it.b.size == 1)
                _screenUIState.value = _screenUIState.value.copy(
                    isDeleteMode = false
                )
        }

        _curMatrix.value = _curMatrix.value.copy(
            b = newB,
            c = newC
        )
    }

    private fun deleteA(index: Int) = viewModelScope.launch {
        val newA = _curMatrix.value.a.filterIndexed { i, _ -> i != index }

        val newC = _curMatrix.value.c.filterIndexed { i, _ ->
            i != index
        }

        _curMatrix.value.let {
            if (it.a.size + it.b.size == 1)
                _screenUIState.value = _screenUIState.value.copy(
                    isDeleteMode = false
                )
        }

        _curMatrix.value = _curMatrix.value.copy(
            a = newA,
            c = newC
        )
    }

    private fun changeScreenMode(mode: ScreenMode) = viewModelScope.launch {
        _screenUIState.value = _screenUIState.value.copy(
            screenMode = mode,
            title = matchTitle(mode)
        )
    }

    private fun switchDeleteMode() = viewModelScope.launch {
        _screenUIState.value = _screenUIState.value.copy(
            isDeleteMode = !_screenUIState.value.isDeleteMode
        )
    }

    private fun findSolution() = viewModelScope.launch {

        _screenUIState.value = _screenUIState.value.copy(
            solution = null
        )

        val calculation = _screenUIState.value.solutionMode.func
        val matrix = _curMatrix.value

        _screenUIState.value = _screenUIState.value.copy(
            solution = calculation(matrix)
        )
    }

    private fun switchSolutionMode() = viewModelScope.launch {
        _screenUIState.value = _screenUIState.value.copy(
            solutionMode = SolutionMode.entries[
                (SolutionMode.entries.indexOf(_screenUIState.value.solutionMode) + 1) % 3
            ]
        )
    }

    init {

        viewModelScope.let {

            it.launch {
                _screenUIState.collectLatest { el ->
                    Log.d("XXXXX", "${el.solution?.first}")
                }
            }

            it.launch {
                _curMatrix.collectLatest { matrix ->
                    _screenUIState.value = _screenUIState.value.copy(
                        isReady = matrix.a.let { it.all { e -> e != null } && it.isNotEmpty() } && matrix.b.let { it.all { e -> e != null } && it.isNotEmpty() } && matrix.c.all { list ->
                            list.all { item ->
                                item.c != null
                            }
                        }
                    )
                }
            }

        }
    }
}