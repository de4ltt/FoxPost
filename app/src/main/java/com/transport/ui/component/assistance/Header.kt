package com.transport.ui.component.assistance

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.transport.ui.theme.Black
import com.transport.ui.theme.TextUnits

@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String
) {

    Crossfade(
        modifier = modifier.fillMaxWidth(),
        targetState = title,
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