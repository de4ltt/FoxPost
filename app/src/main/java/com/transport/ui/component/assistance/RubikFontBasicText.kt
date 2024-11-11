package com.transport.ui.component.assistance

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.text.BasicText

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.text.TextStyle
import com.transport.ui.theme.ExtendedTheme

/**
 * Текст со шрифтом Rubik и basicMarquee
 *
 * @param text текст
 * @param modifier модификатор
 * @param color цвет текста
 * @param style стиль текста
 *
 * @author Михаил Гонтарев (KiREHwYE)
 */
@Composable
fun RubikFontBasicText(
    text: String,
    modifier: Modifier = Modifier,
    basicMarqueeEnabled: Boolean = true,
    color: ColorProducer? = null,
    style: TextStyle
) {
    BasicText(
        text = text,
        color = color,
        style = style
            .copy(
                fontFamily = ExtendedTheme.extendedFonts.rubikFontFamily,
            ),
        modifier = modifier
            .then(
                if (basicMarqueeEnabled)
                    Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE,
                        animationMode = MarqueeAnimationMode.Immediately
                    )
                else Modifier
            )
    )
}