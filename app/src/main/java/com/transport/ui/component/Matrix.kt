package com.transport.ui.component

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.transport.R
import com.transport.model.event.MatrixUIEvent
import com.transport.ui.theme.Grey
import com.transport.ui.theme.LightGrey
import com.transport.ui.theme.Peach
import com.transport.ui.theme.Red
import com.transport.ui.viewmodel.MainViewModel
import kotlin.math.round

@Composable
fun Matrix(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {

    val state by mainViewModel.curMatrix.collectAsStateWithLifecycle()
    val onEvent = mainViewModel::onEvent

    val contentSpacing = 5.dp

    var _tileSize: Dp by remember {
        mutableStateOf(0.dp)
    }
    val tileSize by animateDpAsState(
        targetValue = _tileSize,
        label = ""
    )

    val localDensity = LocalDensity.current
    val orientation = LocalConfiguration.current.orientation
    val lazyColumnState = rememberLazyListState()

    LaunchedEffect(!lazyColumnState.isScrollInProgress) {
        lazyColumnState.animateScrollToItem(lazyColumnState.firstVisibleItemIndex)
    }

    state.apply {

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .onGloballyPositioned { layout ->

                    val isLandscape = orientation == ORIENTATION_LANDSCAPE
                    val width = if (isLandscape) layout.size.height else layout.size.width
                    val size = a.size
                    val tileQuantity = if (isLandscape)
                        when (size) {
                            in 1..2 -> 3
                            else -> 4
                        }
                    else
                        when (size) {
                            in 1..4 -> 6
                            9 -> 10
                            else -> size + 2
                        }
                    val spacing = with(localDensity) { contentSpacing.toPx() }

                    _tileSize = with(localDensity) {
                        calculateTileSize(
                            width = width,
                            spacing = spacing,
                            tileQuantity = tileQuantity
                        ).toDp()
                    }

                    Log.d("SIZE", "$_tileSize")
                },
            state = lazyColumnState,
            verticalArrangement = Arrangement.spacedBy(contentSpacing),
            contentPadding = PaddingValues(vertical = contentSpacing)
        ) {

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(contentSpacing)
                ) {
                    MatrixTile(modifier = Modifier.size(tileSize), isCorner = true)

                    a.forEachIndexed { aIndex, aValue ->
                        MatrixTile(
                            modifier = Modifier.size(tileSize),
                            textValue = aValue.toString(),
                            defaultValue = "A${(aIndex + 1).minify()}"
                        )
                    }

                    MatrixTile(
                        modifier = Modifier.size(tileSize),
                        onClick = { onEvent(MatrixUIEvent.AddA) }
                    )
                }
            }

            b.forEachIndexed { bIndex, bValue ->
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(contentSpacing)
                    ) {
                        MatrixTile(
                            modifier = Modifier.size(tileSize),
                            textValue = bValue.toString(),
                            defaultValue = "B${(bIndex + 1).minify()}"
                        )

                        c[bIndex].forEachIndexed { cIndex, item ->
                            MatrixTile(
                                modifier = Modifier.size(tileSize),
                                textValue = item.toString(),
                                defaultValue = "C${(10 * (bIndex + 1) + cIndex + 1).minify()}"
                            )
                        }
                    }
                }
            }

            item {
                MatrixTile(
                    modifier = Modifier.size(tileSize),
                    onClick = { onEvent(MatrixUIEvent.AddB) }
                )
            }
        }
    }
}

@Composable
private fun MatrixTile(
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
                .clip(tileShape)
                .background(
                    LightGrey
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onClick() }
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
private fun MatrixTile(
    modifier: Modifier = Modifier,
    textValue: String = "null",
    defaultValue: String,
    highlight: Color? = null
) {

    val tileColor = highlight ?: LightGrey
    val textColor = if (textValue == "null") Grey else Black

    Box(
        modifier = modifier
            .clip(tileShape)
            .background(tileColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (textValue == "null") defaultValue else textValue,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }
}

private fun Int.minify(): String {
    val subscriptDigits = mapOf(
        0 to "₀",
        1 to "₁",
        2 to "₂",
        3 to "₃",
        4 to "₄",
        5 to "₅",
        6 to "₆",
        7 to "₇",
        8 to "₈",
        9 to "₉"
    )

    return when {
        this in 0..9 -> subscriptDigits[this]!!
        this > 9 -> this.toString().map { it.digitToInt().minify() }.joinToString("")
        else -> throw IllegalArgumentException("Index is less that 0")
    }
}

private fun calculateTileSize(
    width: Int,
    spacing: Float,
    tileQuantity: Int = 4
): Float = round(width / tileQuantity - spacing)

private val cornerRad: Dp = 6.dp

private val cornerTileShape = RoundedCornerShape(topStart = cornerRad * 3)
private val tileShape = RoundedCornerShape(cornerRad)