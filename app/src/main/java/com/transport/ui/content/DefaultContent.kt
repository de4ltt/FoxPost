package com.transport.ui.content

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.transport.model.event.AppUIEvent
import com.transport.model.state.ScreenMode
import com.transport.model.state.SolutionMode
import com.transport.ui.component.assistance.MethodChoosingButton
import com.transport.ui.component.main.InitialDataTile
import com.transport.ui.theme.Dimens

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DefaultMainContent(
    modifier: Modifier,
    method: SolutionMode,
    onEvent: (AppUIEvent) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {

    with (sharedTransitionScope) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.uniSpacing)
            ) {
                MethodChoosingButton(
                    modifier = Modifier
                        .sharedBounds(
                            rememberSharedContentState(key = "orange"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(),
                            clipInOverlayDuringTransition = OverlayClip(
                                RoundedCornerShape(50)
                            )
                        ),
                    method = method,
                    onEvent = onEvent
                )
                InitialDataTile(
                    onClick = {
                        onEvent(
                            AppUIEvent.ChangeScreenMode(
                                ScreenMode.INITIAL_DATA
                            )
                        )
                    }
                )
            }
        }
    }
}