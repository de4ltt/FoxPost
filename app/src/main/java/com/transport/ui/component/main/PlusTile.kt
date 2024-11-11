package com.transport.ui.component.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.transport.R
import com.transport.ui.theme.Grey
import com.transport.ui.theme.LightGrey
import com.transport.ui.util.bounceClick

@Composable
fun PlusTile(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {

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

private val cornerRad: Dp = 6.dp

private val cornerTileShape = RoundedCornerShape(topStart = cornerRad * 3)
private val tileShape = RoundedCornerShape(cornerRad)