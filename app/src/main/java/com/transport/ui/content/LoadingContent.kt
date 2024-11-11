package com.transport.ui.content

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.transport.model.event.AppUIEvent

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun LoadingContent(
    modifier: Modifier,
    onEvent: (AppUIEvent) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {

}