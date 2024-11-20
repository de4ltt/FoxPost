package com.transport.ui.component.display

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.transport.ui.component.assistance.RubikFontBasicText
import com.transport.ui.theme.Black
import com.transport.ui.theme.LightGrey
import com.transport.ui.theme.extendedFonts

@Composable
fun ABDisplayTile(
    modifier: Modifier = Modifier,
    textValue: String = "",
) {

    val textStyle = TextStyle(
        fontWeight = FontWeight.W400,
        color = Black,
        fontFamily = extendedFonts.rubikFontFamily,
        fontSize = 18.sp,
        lineHeight = 18.sp,
        textAlign = TextAlign.Center
    )

    Box(
        modifier = modifier
            .clip(tileShape)
            .background(LightGrey),
        contentAlignment = Alignment.Center
    ) {
        RubikFontBasicText(
            text = textValue,
            style = textStyle
        )
    }

}

private val cornerRad: Dp = 6.dp

private val cornerTileShape = RoundedCornerShape(topStart = cornerRad * 3)
private val tileShape = RoundedCornerShape(cornerRad)