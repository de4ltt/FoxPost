package com.transport.ui.component.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.transport.ui.component.assistance.RubikFontBasicText
import com.transport.ui.theme.Grey
import com.transport.ui.theme.LightGrey
import com.transport.ui.theme.Red
import com.transport.ui.theme.extendedFonts
import com.transport.ui.util.Keyboard
import com.transport.ui.util.Validator.onlyDigitsLessThan1000
import com.transport.ui.util.keyboardAsState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ABTile(
    modifier: Modifier = Modifier,
    textValue: String = "",
    defaultValue: String,
    onValueChange: (Int?) -> Unit,
    isDeleteMode: Boolean = false,
    onDeletePress: () -> Unit
) {

    val context = LocalContext.current

    val tileInteractionSource = remember { MutableInteractionSource() }

    val isPressed = tileInteractionSource.collectIsPressedAsState().value

    val tileColor by animateColorAsState(
        targetValue = if (isPressed) Red else LightGrey
    )

    var isComposed by remember {
        mutableStateOf(false)
    }

    val scale by animateFloatAsState(targetValue = if (isComposed) 1f else 0f)

    LaunchedEffect(Unit) {
        isComposed = true
    }

    val ab = if (textValue == "null") "" else textValue

    val textColor = if (ab.isEmpty()) Grey else Color.Black

    val textStyle = TextStyle(
        fontWeight = FontWeight.W400,
        color = textColor,
        fontFamily = extendedFonts.rubikFontFamily,
        fontSize = 18.sp,
        lineHeight = 18.sp,
        textAlign = TextAlign.Center
    )

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val keyBoardState by keyboardAsState()

    LaunchedEffect(keyBoardState) {
        if (keyBoardState == Keyboard.Closed)
            focusManager.clearFocus()
    }

    Box(
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .clip(tileShape)
            .background(tileColor)
            .focusRequester(focusRequester)
            .combinedClickable(
                interactionSource = tileInteractionSource,
                indication = ripple({ Red }),
                onClick = {},
                onLongClick = { onDeletePress() }
            ),
        contentAlignment = Alignment.Center
    ) {

        if (isDeleteMode)
            RubikFontBasicText(
                text = if (ab.isNotEmpty()) ab else defaultValue,
                style = textStyle
            )
        else
            BasicTextField(
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.NumberPassword
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                enabled = !isDeleteMode,
                readOnly = isDeleteMode,
                value = ab,
                onValueChange = { input ->
                    if (onlyDigitsLessThan1000(context, input)) {
                        onValueChange(if (input.isEmpty()) null else input.toInt())
                    }
                },
                textStyle = textStyle,
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .focusRequester(focusRequester),
                        contentAlignment = Alignment.Center
                    ) {
                        if (ab.isEmpty())
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
}

private val cornerRad: Dp = 6.dp

private val cornerTileShape = RoundedCornerShape(topStart = cornerRad * 3)
private val tileShape = RoundedCornerShape(cornerRad)