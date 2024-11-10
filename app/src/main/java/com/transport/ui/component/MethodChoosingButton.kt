package com.transport.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import com.transport.ui.theme.Dimens
import com.transport.ui.theme.Peach
import com.transport.ui.theme.Red
import com.transport.ui.theme.TextUnits
import com.transport.ui.util.bounceClick

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MethodChoosingButton(
    modifier: Modifier = Modifier
) {

    val labels = listOf(
        "Метод минимального элемента",
        "Метод северо-западного угла",
        "Метод двойного предпочтения"
    )

    var currentLabelIndex by rememberSaveable {
        mutableStateOf(0)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .bounceClick { currentLabelIndex = (currentLabelIndex + 1) % 3 }
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

        AnimatedContent(targetState = currentLabelIndex, label = "Method") {
            RubikFontBasicText(
                text = labels[it],
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnits.METHOD,
                    color = Color.White
                )
            )
        }
    }
}