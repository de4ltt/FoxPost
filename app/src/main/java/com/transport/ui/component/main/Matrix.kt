package com.transport.ui.component.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.transport.model.event.AppUIEvent
import com.transport.ui.theme.Dimens
import com.transport.ui.util.calculateTileSize
import com.transport.ui.util.minifyDigits
import com.transport.ui.viewmodel.MainViewModel
import kotlin.math.max

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Matrix(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {

    val state by mainViewModel.curMatrix.collectAsStateWithLifecycle()
    val screenState by mainViewModel.screenUIState.collectAsStateWithLifecycle()
    val onEvent = mainViewModel::onEvent

    val contentSpacing = 5.dp

    val localDensity = LocalDensity.current

    state.apply {

        val tileSize by animateDpAsState(
            targetValue = state.tileSize,
            label = ""
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .onGloballyPositioned { layout ->

                    val width = layout.size.width
                    val tileQuantity = when (max(a.size, b.size)) {
                        in 0..3 -> 5
                        9 -> 10
                        else -> max(a.size, b.size) + 2
                    }
                    val spacing = with(localDensity) { contentSpacing.toPx() }

                    val __tileSize = with(localDensity) {
                        calculateTileSize(
                            width = width,
                            spacing = spacing,
                            tileQuantity = tileQuantity
                        ).toDp()
                    }

                    onEvent(
                        AppUIEvent.UpdateTileSize(__tileSize)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .animateContentSize(tween(200)),
                verticalArrangement = Arrangement.spacedBy(contentSpacing)
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(contentSpacing)
                ) {
                    with(sharedTransitionScope) {
                        CornerTile(
                            modifier = Modifier
                                .sharedBounds(
                                    rememberSharedContentState(key = "orange"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(),
                                    clipInOverlayDuringTransition = OverlayClip(
                                        RoundedCornerShape(topStart = Dimens.uniCorners)
                                    )
                                )
                                .size(tileSize),
                            isDeleteMode = screenState.isDeleteMode,
                            onClick = {
                                onEvent(AppUIEvent.SwitchDeleteMode)
                            }
                        )
                    }

                    b.forEachIndexed { bIndex, bValue ->
                        ABTile(
                            modifier = Modifier.size(tileSize),
                            textValue = bValue.toString(),
                            defaultValue = "B${(bIndex + 1).minifyDigits()}",
                            isDeleteMode = screenState.isDeleteMode,
                            onValueChange = {
                                onEvent(AppUIEvent.ChangeB(bIndex, it))
                            },
                            onDeletePress = { onEvent(AppUIEvent.DeleteB(bIndex)) }
                        )
                    }

                    AnimatedVisibility(
                        visible = b.size < 9 && !screenState.isDeleteMode,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        PlusTile(
                            modifier = Modifier.size(tileSize),
                            onClick = { onEvent(AppUIEvent.AddB) }
                        )
                    }
                }


                a.forEachIndexed { aIndex, aValue ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(contentSpacing)
                    ) {

                        ABTile(
                            modifier = Modifier.size(tileSize),
                            textValue = aValue.toString(),
                            defaultValue = "A${(aIndex + 1).minifyDigits()}",
                            isDeleteMode = screenState.isDeleteMode,
                            onValueChange = {
                                onEvent(AppUIEvent.ChangeA(aIndex, it))
                            },
                            onDeletePress = {
                                onEvent(AppUIEvent.DeleteA(aIndex))
                            }
                        )

                        c[aIndex].forEachIndexed { cIndex, item ->
                            MatrixTile(
                                modifier = Modifier.size(tileSize),
                                textValue = item.c.toString(),
                                defaultValue = "C${(10 * (aIndex + 1) + cIndex + 1).minifyDigits()}",
                                onValueChange = {
                                    onEvent(AppUIEvent.ChangeC(Pair(aIndex, cIndex), it))
                                }
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = a.size < 9 && !screenState.isDeleteMode,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    PlusTile(
                        modifier = Modifier.size(tileSize),
                        onClick = { onEvent(AppUIEvent.AddA) }
                    )
                }
            }
        }
    }
}