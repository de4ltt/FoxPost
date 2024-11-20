package com.transport.ui.component.display

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.transport.ui.component.assistance.RubikFontBasicText
import com.transport.ui.theme.Black
import com.transport.ui.theme.Grey
import com.transport.ui.theme.LightGrey
import com.transport.ui.theme.White

@Composable
fun DisplayMatrixTile(
    modifier: Modifier = Modifier,
    highlight: Color? = null,
    distance: Int,
    eval: Int,
    c: Int,
    x: Int,
    d: Int?,
    check: Pair<Boolean, Boolean>?
) {

    val tileColor = highlight ?: LightGrey
    val textColor = if (highlight != null) White else Grey
    val xColor = if (highlight != null) White else Black

    Row(
        modifier = modifier
            .clip(tileShape)
            .background(tileColor)
            .background(tileColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (check != null) {
                Checks(
                    modifier = Modifier.weight(1f),
                    color = { xColor },
                    check = check
                )
            }

            if (eval < 0)
                RubikFontBasicText(
                    text = "$eval",
                    style = TextStyle(
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        textAlign = TextAlign.Center
                    )
                )

            RubikFontBasicText(
                modifier = Modifier.weight(1f),
                text = "$x",
                style = TextStyle(
                    color = xColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Center
                )
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RubikFontBasicText(
                text = "$c",
                style = TextStyle(
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    textAlign = TextAlign.Center
                )
            )

            d?.let {
                RubikFontBasicText(
                    text = if (it > 0) "+$d" else "$d",
                    style = TextStyle(
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

private val cornerRad: Dp = 6.dp

private val cornerTileShape = RoundedCornerShape(topStart = cornerRad * 3)
private val tileShape = RoundedCornerShape(cornerRad)