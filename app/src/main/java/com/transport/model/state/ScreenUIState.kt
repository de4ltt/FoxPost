package com.transport.model.state

import com.transport.ui.theme.Strings

enum class ScreenMode {
    DEFAULT,
    INITIAL_DATA,
    LOADING_SOLUTION,
    SOLUTION
}

data class ScreenUIState(
    val isReady: Boolean = false,
    val title: String = Strings.INITIAL_CONDITIONS,
    val screenMode: ScreenMode = ScreenMode.DEFAULT
)