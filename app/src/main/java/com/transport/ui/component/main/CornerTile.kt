package com.transport.ui.component.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.transport.R
import com.transport.ui.theme.Dimens.uniPadding
import com.transport.ui.theme.Peach
import com.transport.ui.theme.Red
import com.transport.ui.theme.White
import com.transport.ui.util.bounceClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CornerTile(
    modifier: Modifier = Modifier,
    isDeleteMode: Boolean = false,
    onClick: () -> Unit = {}
) {

    val alphaScale by animateFloatAsState(targetValue = if (isDeleteMode) 1f else 0f)

    Box(
        modifier = modifier
            .clip(cornerTileShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Peach, Red)
                )
            )
            .bounceClick { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.trash),
            contentDescription = "",
            tint = { White },
            modifier = Modifier
                .fillMaxSize(0.5f)
                .graphicsLayer(
                    scaleX = alphaScale,
                    scaleY = alphaScale,
                    alpha = alphaScale
                )
        )
    }
}

private val cornerRad: Dp = 6.dp

private val cornerTileShape = RoundedCornerShape(topStart = cornerRad * 3)