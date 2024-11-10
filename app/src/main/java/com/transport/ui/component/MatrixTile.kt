package com.transport.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.transport.R
import com.transport.ui.theme.Grey
import com.transport.ui.theme.LightGrey
import com.transport.ui.theme.Peach
import com.transport.ui.theme.Red
import com.transport.ui.util.bounceClick

@Composable
fun MatrixTile(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    isCorner: Boolean = false
) {

    if (isCorner)
        Box(
            modifier = modifier
                .clip(cornerTileShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Peach, Red)
                    )
                )
        )
    else
        Box(
            modifier = modifier
                .bounceClick(
                    onClick = { onClick() }
                )
                .clip(tileShape)
                .background(
                    LightGrey
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus),
                modifier = Modifier.fillMaxSize(0.3f),
                tint = Grey,
                contentDescription = "plus"
            )
        }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MatrixTile(
    modifier: Modifier = Modifier,
    textValue: String = "null",
    defaultValue: String,
    highlight: Color? = null,
    onLongClick: () -> Unit = { }
) {

    val tileColor = highlight ?: LightGrey
    val textColor = if (textValue == "null") Grey else Color.Black

    var isComposed by remember {
        mutableStateOf(false)
    }

    val scale by animateFloatAsState(targetValue = if (isComposed) 1f else 0f)

    LaunchedEffect(Unit) {
        isComposed = true
    }

    Box(
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .clip(tileShape)
            .background(tileColor)
            /*.combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onLongClick = { onLongClick() }
            ) {}*/,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (textValue == "null") defaultValue else textValue,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }
}

private val cornerRad: Dp = 6.dp

private val cornerTileShape = RoundedCornerShape(topStart = cornerRad * 3)
private val tileShape = RoundedCornerShape(cornerRad)