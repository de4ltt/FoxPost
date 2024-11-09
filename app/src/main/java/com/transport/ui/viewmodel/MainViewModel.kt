package com.transport.ui.viewmodel

import android.util.Log
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

    }

    private fun changeB(index: Int, newValue: Int) = viewModelScope.launch {

    }

    private fun changeC(position: Pair<Int, Int>, newValue: Int) = viewModelScope.launch {

    }

}