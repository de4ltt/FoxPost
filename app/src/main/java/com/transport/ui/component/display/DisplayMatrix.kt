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
import com.transport.ui.theme.Dimens.tileSpacing
import com.transport.ui.theme.Dimens.uniSpacedBy
import com.transport.ui.theme.Dimens.uniSpacing
import com.transport.ui.theme.Red
import com.transport.ui.theme.TextUnits

@Composable
fun DisplayMatrix(
    modifier: Modifier = Modifier,
    description: String,
    matrix: Matrix?
) {

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

        matrix?.let {

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(tileSpacing)
                ) {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(tileSpacing)
                    ) {
                        CornerTile(
                            modifier = Modifier.size(it.tileSize)
                        )

                        it.b.forEach { bValue ->
                            ABDisplayTile(
                                modifier = Modifier.size(it.tileSize),
                                textValue = bValue.toString(),
                            )
                        }
                    }

                    it.a.forEachIndexed { aIndex, aValue ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(tileSpacing)
                        ) {
                            ABDisplayTile(
                                modifier = Modifier.size(it.tileSize),
                                textValue = aValue.toString(),
                            )

                            it.c[aIndex].forEachIndexed { cIndex, item ->
                                DisplayMatrixTile(
                                    modifier = Modifier.size(it.tileSize),
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