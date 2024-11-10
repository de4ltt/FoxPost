package com.transport.ui.component

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.transport.R
import com.transport.ui.theme.Grey
import com.transport.ui.theme.LightGrey
import com.transport.ui.theme.Peach
import com.transport.ui.theme.Red
import com.transport.ui.theme.TransportTheme
import com.transport.ui.theme.White
import com.transport.ui.theme.extendedFonts
import com.transport.ui.util.Validator
import com.transport.ui.util.bounceClick

@Composable
fun MatrixTile(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    isCorner: Boolean = false
) {

    if (isCorner)
        Box(
            modifier = modifier
                .clip(cornerTileShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Peach, Red)
                    )
                )
        )
    else
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

@Composable
fun MatrixTile(
    modifier: Modifier = Modifier,
    textValue: String = "",
    defaultValue: String,
    highlight: Color? = null,
    onLongClick: () -> Unit = { }
) {

    val context = LocalContext.current

    val tileColor = highlight ?: LightGrey
    val textColor = if (textValue.isEmpty() || textValue == "null") Grey else Color.Black

    var isComposed by remember {
        mutableStateOf(false)
    }

    val scale by animateFloatAsState(targetValue = if (isComposed) 1f else 0f)

    LaunchedEffect(Unit) {
        isComposed = true
    }

    var c by rememberSaveable {
        mutableStateOf(textValue)
    }

    var isInputEnabled by rememberSaveable {
        mutableStateOf(false)
    }

    val textStyle = TextStyle(
        fontWeight = FontWeight.W400,
        color = textColor,
        fontFamily = extendedFonts.rubikFontFamily,
        fontSize = 18.sp,
        lineHeight = 18.sp,
        textAlign = TextAlign.Center
    )

    BasicTextField(
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .clip(tileShape)
            .background(tileColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isInputEnabled = !isInputEnabled
                    },
                    onLongPress = {
                        onLongClick()
                    }
                )
            },
        enabled = isInputEnabled,
        value = c,
        onValueChange = {
            Validator.onlyDigits(context, it) { c = it }
        },
        textStyle = textStyle,
        singleLine = true,
        decorationBox = { innerTextField ->

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (!isInputEnabled)
                    RubikFontBasicText(
                        text = defaultValue,
                        style = textStyle
                    )
                else
                    innerTextField()
            }
        }
    )

}

@Composable
fun MatrixTile(
    modifier: Modifier = Modifier,
    highlight: Color? = null,
    distance: Int,
    c: Int,
    x: Int,
    d: Int
) {

    val tileColor = highlight ?: LightGrey
    val textColor = if (highlight != null) White else Grey

    var isComposed by remember {
        mutableStateOf(false)
    }

    val scale by animateFloatAsState(
        targetValue = if (isComposed) 1f else 0f,
        animationSpec = tween(200 + distance * 100)
    )

    LaunchedEffect(Unit) {
        isComposed = true
    }

    Row(
        modifier = modifier
            .clip(tileShape)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .background(tileColor)
            .background(tileColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RubikFontBasicText(
            modifier = Modifier.weight(1f),
            text = "$x",
            style = TextStyle(
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                textAlign = TextAlign.Center
            )
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RubikFontBasicText(
                text = "$c",
                style = TextStyle(
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    textAlign = TextAlign.Center
                )
            )

            RubikFontBasicText(
                text = if (d > 0) "+$d" else "$d",
                style = TextStyle(
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

private val cornerRad: Dp = 6.dp

private val cornerTileShape = RoundedCornerShape(topStart = cornerRad * 3)
private val tileShape = RoundedCornerShape(cornerRad)