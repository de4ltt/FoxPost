package com.transport.model.state

import com.transport.algorithm.calculateSolutionDoublePreferenceMethod
import com.transport.algorithm.calculateSolutionMinimalElementMethod
import com.transport.algorithm.calculateSolutionNorthWestAngleMethod
import com.transport.model.Matrix
import com.transport.ui.theme.Strings

enum class ScreenMode {
    DEFAULT,
    INITIAL_DATA,
    SOLUTION
}

enum class SolutionMode(
    val title: String,
    val func: (Matrix) -> List<Pair<String, Matrix?>>
) {
    NW_ANGLE(Strings.NW_ANGLE, { calculateSolutionNorthWestAngleMethod(it)}),
    MIN_ELEMENT(Strings.MIN_ELEMENT, { calculateSolutionMinimalElementMethod(it) }),
    DOUBLE_PREFERENCE(Strings.DOUBLE_PREFERENCE, { calculateSolutionDoublePreferenceMethod(it) })
}

data class ScreenUIState(
    val isDeleteMode: Boolean = false,
    val isReady: Boolean = false,
    val title: String = Strings.INITIAL_CONDITIONS_MATRIX,
    val screenMode: ScreenMode = ScreenMode.DEFAULT,

    val solutionMode: SolutionMode = SolutionMode.NW_ANGLE,
    val solution: List<Pair<String, Matrix?>> = emptyList()
)