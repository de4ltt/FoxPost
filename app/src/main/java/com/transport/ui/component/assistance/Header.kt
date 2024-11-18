package com.transport.ui.component.assistance

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.transport.ui.theme.Black
import com.transport.ui.theme.Dimens
import com.transport.ui.theme.TextUnits
import com.transport.ui.theme.White

@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(White)
            .windowInsetsPadding(WindowInsets.displayCutout)
            .padding(vertical = Dimens.uniPadding)
    ) {
        AnimatedContent(
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
}