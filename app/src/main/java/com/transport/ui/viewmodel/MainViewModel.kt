package com.transport.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.transport.model.Matrix
import com.transport.model.event.MatrixUIEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _curMatrix: MutableStateFlow<Matrix> = MutableStateFlow(Matrix())
    val curMatrix = _curMatrix.asStateFlow()

    fun onEvent(event: MatrixUIEvent) = when (event) {
        MatrixUIEvent.AddA -> addA()
        MatrixUIEvent.AddB -> addB()
        is MatrixUIEvent.ChangeA -> changeA(event.index, event.newValue)
        is MatrixUIEvent.ChangeB -> changeB(event.index, event.newValue)
        is MatrixUIEvent.ChangeC -> changeC(event.position, event.newValue)
        is MatrixUIEvent.DeleteA -> deleteA(event.index)
        is MatrixUIEvent.DeleteB -> deleteB(event.index)
    }

    private fun addA() = viewModelScope.launch {
        _curMatrix.value = _curMatrix.value.copy(
            a = _curMatrix.value.a + null
        )

        _curMatrix.value = _curMatrix.value.copy(
            c = _curMatrix.value.c.map {
                it + null
            }
        )
    }

    private fun addB() = viewModelScope.launch {
        _curMatrix.value = _curMatrix.value.copy(
            b = _curMatrix.value.b + null
        )


        _curMatrix.value = _curMatrix.value.copy(
            c = _curMatrix.value.c + listOf(List<Int?>(_curMatrix.value.a.size) { null })
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
                        newValue
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

        val newC = _curMatrix.value.c.mapIndexed { i, value ->
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
        val newB = _curMatrix.value.b.filterIndexed() { i, _ -> i != index }

        val newC = _curMatrix.value.c.filterIndexed { i, value ->
            i != index
        }

        _curMatrix.value = _curMatrix.value.copy(
            b = newB,
            c = newC
        )
    }
}