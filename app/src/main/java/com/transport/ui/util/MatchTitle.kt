package com.transport.ui.util

import com.transport.model.state.ScreenMode
import com.transport.ui.theme.Strings

fun matchTitle(screenMode: ScreenMode): String = when (screenMode) {
    ScreenMode.DEFAULT -> Strings.INITIAL_CONDITIONS_MATRIX
    ScreenMode.INITIAL_DATA -> Strings.INITIAL_DATA_MATRIX
    ScreenMode.LOADING_SOLUTION -> Strings.SOLUTION
    ScreenMode.SOLUTION -> Strings.SOLUTION
}