package com.transport.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import com.transport.ui.theme.Black

import com.transport.ui.theme.Dimens
import com.transport.ui.theme.LightGrey
import com.transport.ui.theme.Peach
import com.transport.ui.theme.Red
import com.transport.ui.theme.Strings
import com.transport.ui.theme.TextUnits
import com.transport.ui.theme.White
import com.transport.ui.util.bounceClick

@Composable
fun ReadyButton(
    modifier: Modifier = Modifier,
    text: String = Strings.READY,
    isActive: Boolean = false,
    isVisible: Boolean = true,
    onClick: () -> Unit = { },
) {

    val colors = listOf(
        animateColorAsState(
            targetValue = if (isActive) Peach else LightGrey,
            label = "", animationSpec = tween(200)
        ).value,
        animateColorAsState(
            targetValue = if (isActive) Red else LightGrey,
            label = "", animationSpec = tween(400)
        ).value
    )

    val textColor by animateColorAsState(
        targetValue = if (isActive) White else Black,
        label = ""
    )

    AnimatedContent(
        targetState = isVisible,
        transitionSpec = {
            (fadeIn() + slideInVertically(animationSpec = spring()))
                .togetherWith(slideOutVertically(animationSpec = spring()) + fadeOut())
        },
        label = ""
    ) {
        if (it)
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(Dimens.uniCorners))
                    .bounceClick(
                        enabled = isActive,
                        onClick = { onClick() }
                    )
                    .fillMaxWidth()
                    .drawBehind {
                        drawRect(
                            Brush.linearGradient(
                                colors = colors
                            )
                        )
                    }
                    .padding(Dimens.uniHorizontalPadding),
                contentAlignment = Alignment.Center
            ) {

                RubikFontBasicText(
                    text = text,
                    color = { textColor },
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = TextUnits.READY
                    )
                )
            }
    }
}