package com.transport.ui.viewmodel

import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.transport.model.CTile
import com.transport.model.Matrix
import com.transport.model.event.MatrixUIEvent
import com.transport.model.state.ScreenMode
import com.transport.model.state.ScreenUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _curMatrix: MutableStateFlow<Matrix> = MutableStateFlow(Matrix())
    val curMatrix = _curMatrix.asStateFlow()

    private val _screenUIState: MutableStateFlow<ScreenUIState> = MutableStateFlow(ScreenUIState())
    val screenUIState = _screenUIState.asStateFlow()

    fun onEvent(event: MatrixUIEvent) = when (event) {
        MatrixUIEvent.AddA -> addA()
        MatrixUIEvent.AddB -> addB()
        is MatrixUIEvent.ChangeA -> changeA(event.index, event.newValue)
        is MatrixUIEvent.ChangeB -> changeB(event.index, event.newValue)
        is MatrixUIEvent.ChangeC -> changeC(event.position, event.newValue)
        is MatrixUIEvent.DeleteA -> deleteA(event.index)
        is MatrixUIEvent.DeleteB -> deleteB(event.index)

        is MatrixUIEvent.ChangeScreenMode -> changeScreenMode(event.screenMode)
        is MatrixUIEvent.UpdateTileSize -> updateTileSize(event.tileSize)
    }

    private fun updateTileSize(tileSize: Dp) = viewModelScope.launch {
        _curMatrix.value = _curMatrix.value.copy(
            tileSize = tileSize
        )
    }

    private fun addA() = viewModelScope.launch {
        _curMatrix.value = _curMatrix.value.copy(
            a = _curMatrix.value.a + null
        )

        _curMatrix.value = _curMatrix.value.copy(
            c = _curMatrix.value.c.map {
                it + CTile()
            }
        )
    }

    private fun addB() = viewModelScope.launch {
        _curMatrix.value = _curMatrix.value.copy(
            b = _curMatrix.value.b + null
        )


        _curMatrix.value = _curMatrix.value.copy(
            c = _curMatrix.value.c + listOf(List(_curMatrix.value.a.size) { CTile() })
        )
    }

    private fun changeA(index: Int, newValue: Int) = viewModelScope.launch {
        val newA = _curMatrix.value.a.mapIndexed { i, value ->
            if (i == index) newValue
            else value
        }

        _curMatrix.value = _curMatrix.value.copy(
            a = newA
        )
    }

    private fun changeB(index: Int, newValue: Int) = viewModelScope.launch {
        val newB = _curMatrix.value.b.mapIndexed { i, value ->
            if (i == index) newValue
            else value
        }

        _curMatrix.value = _curMatrix.value.copy(
            b = newB
        )
    }

    private fun changeC(position: Pair<Int, Int>, newValue: Int) = viewModelScope.launch {
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

    private fun deleteA(index: Int) = viewModelScope.launch {
        val newA = _curMatrix.value.a.filterIndexed() { i, _ -> i != index }

        val newC = _curMatrix.value.c.map { value ->
            value.filterIndexed { j, _ ->
                j != index
            }
        }

        _curMatrix.value = _curMatrix.value.copy(
            a = newA,
            c = newC
        )
    }

    private fun deleteB(index: Int) = viewModelScope.launch {
        val newB = _curMatrix.value.b.filterIndexed { i, _ -> i != index }

        val newC = _curMatrix.value.c.filterIndexed { i, _ ->
            i != index
        }

        _curMatrix.value = _curMatrix.value.copy(
            b = newB,
            c = newC
        )
    }

    private fun changeScreenMode(mode: ScreenMode) = viewModelScope.launch {
        _screenUIState.value = _screenUIState.value.copy(
            screenMode = mode
        )
    }


    init {

        viewModelScope.launch {
            _curMatrix.collectLatest { matrix ->
                _screenUIState.value = _screenUIState.value.copy(
                    isReady = matrix.a.all { it != null } && matrix.b.all { it != null } && matrix.c.all { list ->
                        list.all { item ->
                            item.c != null
                        }
                    }
                )
            }
        }
    }
}