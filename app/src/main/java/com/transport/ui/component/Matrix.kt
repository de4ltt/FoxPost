package com.transport.ui.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.transport.model.Matrix
import com.transport.model.event.MatrixUIEvent
import com.transport.ui.theme.Dimens
import com.transport.ui.theme.Red
import com.transport.ui.util.calculateTileSize
import com.transport.ui.util.minifyDigits
import com.transport.ui.viewmodel.MainViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Matrix(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {

    var _scale by remember { mutableFloatStateOf(1f) }
    var _offset by remember { mutableStateOf(Offset.Zero) }

    val scale by animateFloatAsState(targetValue = _scale)
    val offset by animateOffsetAsState(targetValue = _offset)

    val transformableState = rememberTransformableState { scaleChange, offsetChange, _ ->
        _scale *= scaleChange
        _offset += offsetChange
    }

    LaunchedEffect(!transformableState.isTransformInProgress) {
        _scale = 1f
        _offset = Offset.Zero
    }

    val state by mainViewModel.curMatrix.collectAsStateWithLifecycle()
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
                .transformable(transformableState)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .onGloballyPositioned { layout ->

                    val width = layout.size.width
                    val tileQuantity = when (a.size) {
                        in 0..3 -> 5
                        9 -> 10
                        else -> a.size + 2
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
                        MatrixUIEvent.UpdateTileSize(__tileSize)
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
                        MatrixTile(
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
                            isCorner = true
                        )
                    }

                    a.forEachIndexed { aIndex, aValue ->
                        MatrixTile(
                            modifier = Modifier.size(tileSize),
                            textValue = aValue.toString(),
                            defaultValue = "A${(aIndex + 1).minifyDigits()}",
                            onLongClick = { onEvent(MatrixUIEvent.DeleteA(aIndex)) }
                        )
                    }

                    if (a.size < 9)
                        MatrixTile(
                            modifier = Modifier.size(tileSize),
                            onClick = { onEvent(MatrixUIEvent.AddA) }
                        )
                }


                b.forEachIndexed { bIndex, bValue ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(contentSpacing)
                    ) {
                        MatrixTile(
                            modifier = Modifier.size(tileSize),
                            textValue = bValue.toString(),
                            defaultValue = "B${(bIndex + 1).minifyDigits()}",
                            onLongClick = { onEvent(MatrixUIEvent.DeleteB(bIndex)) }
                        )

                        c[bIndex].forEachIndexed { cIndex, item ->
                            MatrixTile(
                                modifier = Modifier.size(tileSize),
//                                textValue = item.c.toString(),
                                textValue = "",
                                defaultValue = "C${(10 * (bIndex + 1) + cIndex + 1).minifyDigits()}"
                            )
                        }
                    }
                }

                if (b.size < 9)
                    MatrixTile(
                        modifier = Modifier.size(tileSize),
                        onClick = { onEvent(MatrixUIEvent.AddB) }
                    )
            }
        }
    }
}

@Composable
fun Matrix(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    matrix: Matrix
) {

    var _scale by remember { mutableFloatStateOf(1f) }
    var _offset by remember { mutableStateOf(Offset.Zero) }

    val scale by animateFloatAsState(targetValue = _scale)
    val offset by animateOffsetAsState(targetValue = _offset)

    val contentSpacing = 5.dp

    val transformableState = rememberTransformableState { scaleChange, offsetChange, _ ->
        _scale *= scaleChange
        _offset += offsetChange
    }

    LaunchedEffect(!transformableState.isTransformInProgress) {
        _scale = 1f
        _offset = Offset.Zero
    }

    val onEvent = mainViewModel::onEvent

    matrix.apply {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .transformable(transformableState)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(contentSpacing)
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(contentSpacing)
                ) {
                    MatrixTile(modifier = Modifier.size(tileSize), isCorner = true)

                    a.forEachIndexed { aIndex, aValue ->
                        MatrixTile(
                            modifier = Modifier.size(tileSize),
                            textValue = aValue.toString(),
                            defaultValue = "A${(aIndex + 1).minifyDigits()}"
                        )
                    }
                }

                b.forEachIndexed { bIndex, bValue ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(contentSpacing)
                    ) {
                        MatrixTile(
                            modifier = Modifier.size(tileSize),
                            textValue = bValue.toString(),
                            defaultValue = "B${(bIndex + 1).minifyDigits()}",
                            onLongClick = { onEvent(MatrixUIEvent.DeleteB(bIndex)) }
                        )

                        c[bIndex].forEachIndexed { cIndex, item ->
                            MatrixTile(
                                modifier = Modifier.size(tileSize),
                                highlight = if (item.d != 0) Red else null,
                                distance = cIndex + bIndex,
                                c = item.c ?: 0,
                                x = item.x ?: 0,
                                d = item.d ?: 0
                            )
                        }
                    }
                }
            }
        }
    }
}