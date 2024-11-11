package com.transport.ui.component.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import com.transport.R
import com.transport.ui.component.assistance.RubikFontBasicText

import com.transport.ui.theme.Cherry
import com.transport.ui.theme.Dimens
import com.transport.ui.theme.RedFoggy
import com.transport.ui.theme.Strings
import com.transport.ui.theme.TextUnits
import com.transport.ui.theme.WhiteFoggy
import com.transport.ui.util.bounceClick

@Composable
fun InitialDataTile(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {

    var tileHeight by rememberSaveable {
        mutableFloatStateOf(0f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f / 1f)
            .clip(RoundedCornerShape(Dimens.uniCorners))
            .paint(
                painter = painterResource(id = R.drawable.workers),
                contentScale = ContentScale.Crop
            )
            .onGloballyPositioned {
                tileHeight = it.size.height.toFloat()
            }
            .drawBehind {
                drawRect(
                    Brush.linearGradient(
                        start = Offset(0f, tileHeight / 3),
                        end = Offset(0f, Float.POSITIVE_INFINITY),
                        colors = listOf(WhiteFoggy, RedFoggy)
                    )
                )
            }
            .padding(Dimens.uniPadding),
        contentAlignment = Alignment.BottomStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(Dimens.uniSpacedBy),
            horizontalAlignment = Alignment.Start
        ) {

            RubikFontBasicText(
                text = Strings.INITIAL_DATA,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = TextUnits.INITIAL_DATA,
                    color = Color.White
                )
            )

            RubikFontBasicText(
                text = Strings.CRINGY_TEXT,
                basicMarqueeEnabled = false,
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnits.CRINGY_TEXT,
                    color = Color.White
                )
            )

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .bounceClick {
                        onClick()
                    }
                    .clip(RoundedCornerShape(Dimens.uniCorners))
                    .background(color = Cherry)
                    .padding(Dimens.enterButtonPadding),
                horizontalArrangement = Arrangement.spacedBy(Dimens.enterButtonSpacedBy),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.pen),
                    contentDescription = "Pen",
                    tint = Color.White,
                    modifier = Modifier
                        .size(Dimens.penSize)
                )

                RubikFontBasicText(
                    text = Strings.ENTER,
                    style = TextStyle(
                        fontWeight = FontWeight.W400,
                        fontSize = TextUnits.ENTER,
                        color = Color.White
                    )
                )
            }
        }
    }
}