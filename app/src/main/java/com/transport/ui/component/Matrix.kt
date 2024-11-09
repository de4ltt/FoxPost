package com.transport.ui.component

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.transport.model.event.MatrixUIEvent
import com.transport.ui.util.calculateTileSize
import com.transport.ui.util.minifyDigits
import com.transport.ui.viewmodel.MainViewModel

@Composable
fun Matrix(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
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

    var _tileSize: Dp by remember {
        mutableStateOf(0.dp)
    }
    val tileSize by animateDpAsState(
        targetValue = _tileSize,
        label = ""
    )

    val localDensity = LocalDensity.current

    state.apply {

        Box(
            modifier = modifier
                .padding(vertical = 20.dp, horizontal = 20.dp)
                .fillMaxSize()
                .transformable(transformableState)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .onGloballyPositioned { layout ->

                val width = layout.size.width
                val size = a.size
                val tileQuantity = when (size) {
                        in 0..3 -> 5
                        9 -> 10
                        else -> size + 2
                    }
                val spacing = with(localDensity) { contentSpacing.toPx() }

                _tileSize = with(localDensity) {
                    calculateTileSize(
                        width = width,
                        spacing = spacing,
                        tileQuantity = tileQuantity
                    ).toDp()
                }
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
                    MatrixTile(modifier = Modifier.size(tileSize), isCorner = true)

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
                                textValue = item.toString(),
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