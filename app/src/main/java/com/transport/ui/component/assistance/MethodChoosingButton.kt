package com.transport.ui.component.assistance

import androidx.compose.animation.AnimatedContent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.transport.model.event.AppUIEvent
import com.transport.model.state.SolutionMode

import com.transport.ui.theme.Dimens
import com.transport.ui.theme.Peach
import com.transport.ui.theme.Red
import com.transport.ui.theme.TextUnits
import com.transport.ui.util.bounceClick

@Composable
fun MethodChoosingButton(
    method: SolutionMode,
    onEvent: (AppUIEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .bounceClick {
                onEvent(AppUIEvent.SwitchSolutionMode)
            }
            .clip(RoundedCornerShape(50))
            .drawBehind {
                drawRect(
                    Brush.linearGradient(
                        colors = listOf(Peach, Red)
                    )
                )
            }
            .padding(Dimens.uniPadding),
        contentAlignment = Alignment.Center
    ) {

        AnimatedContent(targetState = method.title, label = "Method") {
            RubikFontBasicText(
                text = it,
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnits.METHOD,
                    color = Color.White
                )
            )
        }
    }
}