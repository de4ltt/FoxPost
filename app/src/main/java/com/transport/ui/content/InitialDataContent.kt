package com.transport.ui.content

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.transport.model.event.AppUIEvent
import com.transport.model.state.ScreenMode
import com.transport.ui.component.main.Matrix
import com.transport.ui.viewmodel.MainViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun InitialDataContent(
    modifier: Modifier,
    mainViewModel: MainViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        BackHandler {
            mainViewModel.onEvent(
                AppUIEvent.ChangeScreenMode(
                    ScreenMode.DEFAULT
                )
            )
            return@BackHandler
        }

        Matrix(
            mainViewModel = mainViewModel,
            animatedVisibilityScope = animatedVisibilityScope,
            sharedTransitionScope = sharedTransitionScope
        )
    }

}