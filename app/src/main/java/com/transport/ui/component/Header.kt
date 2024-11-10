package com.transport.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.transport.ui.theme.Black
import com.transport.ui.theme.TextUnits

@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String
) {

    AnimatedContent(
        modifier = modifier.fillMaxWidth(),
        targetState = title,
        transitionSpec = {
            (fadeIn() + slideInVertically(tween(200)))
                .togetherWith(slideOutVertically(tween(200)) + fadeOut(tween(200)))
        },
        label = ""
    ) {

        RubikFontBasicText(
            text = it,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = TextUnits.HEADER,
                color = Black,
                lineHeight = TextUnits.HEADER_LINE_HEIGHT
            )
        )
    }
}