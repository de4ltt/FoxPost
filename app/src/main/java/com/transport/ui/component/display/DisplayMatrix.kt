package com.transport.ui.component.display

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.transport.model.Matrix
import com.transport.ui.component.assistance.RubikFontBasicText
import com.transport.ui.component.main.CornerTile
import com.transport.ui.theme.DarkGrey
import com.transport.ui.theme.Dimens.uniSpacedBy
import com.transport.ui.theme.Dimens.uniSpacing
import com.transport.ui.theme.Red
import com.transport.ui.theme.TextUnits

@Composable
fun DisplayMatrix(
    modifier: Modifier = Modifier,
    description: String,
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

    matrix.apply {

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(uniSpacedBy)
        ) {

            RubikFontBasicText(
                text = description,
                basicMarqueeEnabled = false,
                style = TextStyle(
                    color = DarkGrey,
                    fontSize = TextUnits.READY,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic
                )
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
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(contentSpacing)
                ) {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(contentSpacing)
                    ) {
                        CornerTile(
                            modifier = Modifier.size(tileSize)
                        )

                        b.forEach { bValue ->
                            ABDisplayTile(
                                modifier = Modifier.size(tileSize),
                                textValue = bValue.toString(),
                            )
                        }
                    }

                    a.forEachIndexed { aIndex, aValue ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(contentSpacing)
                        ) {
                            ABDisplayTile(
                                modifier = Modifier.size(tileSize),
                                textValue = aValue.toString(),
                            )

                            c[aIndex].forEachIndexed { cIndex, item ->
                                DisplayMatrixTile(
                                    modifier = Modifier.size(tileSize),
                                    highlight = if (item.d != 0 && item.d != null) Red else null,
                                    distance = cIndex + aIndex,
                                    c = item.c ?: 0,
                                    x = item.x ?: 0,
                                    d = item.d ?: 0,
                                    check = item.check
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}