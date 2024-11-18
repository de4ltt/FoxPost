package com.transport.ui.component.assistance

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState

import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

import com.transport.ui.theme.Dimens
import com.transport.ui.theme.Peach
import com.transport.ui.theme.White

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import kotlin.math.roundToInt

/**
 * Контейнер с верхним баром и плавающей кнопкой
 *
 * @param contentIsEmpty флаг того, что ничего нет и нужно оповестить пользователя об этом
 * @param shiftBottomBar сдвигает нижний бар вниз (сам бар за пределами данного контейнера)
 * @param topBar верхний бар
 * @param floatingButton плавающая кнопка
 * @param content основной контент
 *
 * @author Михаил Гонтарев (KiREHwYE)
 */
@Composable
fun ListWithTopAndFab(
    shiftBottomBar: () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    floatingButton: @Composable () -> Unit = {},
    content: @Composable (Modifier, (Boolean) -> Unit) -> Unit = { _, _ -> },
) {
    val coroutineScope = rememberCoroutineScope()

    /** измерения контейнера в пикселях */
    val localDensity = LocalDensity.current

    /** высота верхнего бара в пикселях*/
    val topBarHeightPx = remember { mutableStateOf(0f) }
    /** свдиг верхнего бара по высоте */
    val topBarOffsetHeightPx = remember { mutableStateOf(0f) }
    /** динамический отступ для списка */
    val spaceHeight = rememberDerivedStateOf {
        (topBarHeightPx.value + topBarOffsetHeightPx.value) / localDensity.density
    }
    /** отступ верхней системной панели навигации */
    val topInsetPaddingPx = with(LocalDensity.current) {
        WindowInsets.navigationBars.asPaddingValues().calculateTopPadding().toPx()
    }

    /** высота плавающей кнопки в пикселях */
    val fabWidthPx = remember { mutableStateOf(0f) }
    /** свдиг нижнего бара по высоте */
    val fabOffsetWidthPx = remember { mutableStateOf(0f) }

    /** отступ нижней системной панели навигации */
    val bottomInsetPaddingPx = with(localDensity) {
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding().toPx()
    }

    /** Поднимает верхний бар, скрывая его из области видимости */
    fun shiftTopBarUp() {
        coroutineScope.launch {
            while (-topBarOffsetHeightPx.value < topBarHeightPx.value) {
                val newTopBarOffset = (topBarOffsetHeightPx.value - 5).coerceIn(
                    minimumValue = -topBarHeightPx.value,
                    maximumValue = 0f
                )
                topBarOffsetHeightPx.value = newTopBarOffset
                delay(1)
            }
        }
    }

    /** Опускает верхний бар, делая его полностью видимым */
    fun shiftTopBarDown() {
        coroutineScope.launch {
            while (topBarOffsetHeightPx.value < 0) {
                topBarOffsetHeightPx.value += 5
                delay(1)
            }
        }
    }

    /** слушатель скролла экрана */
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val deltaY = available.y

                val newBottomBarOffset = fabOffsetWidthPx.value + deltaY
                fabOffsetWidthPx.value =
                    newBottomBarOffset.coerceIn(
                        minimumValue = -fabWidthPx.value,
                        maximumValue = 0f
                    )

                var consumed: Offset

                val newTopBarOffset = (topBarOffsetHeightPx.value + deltaY).coerceIn(
                    minimumValue = -topBarHeightPx.value,
                    maximumValue = 0f
                )
                consumed = Offset(0f, newTopBarOffset - topBarOffsetHeightPx.value)
                topBarOffsetHeightPx.value = newTopBarOffset

                return consumed
            }
        }
    }

    val noElementsMessageTopPadding by rememberDerivedStateOf {
        (topBarHeightPx.value / localDensity.density).dp
    }

    var isEmpty by remember {
        mutableStateOf(false)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    // Основной контент
    //////////////////////////////////////////////////////////////////////////////////////////
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
            .nestedScroll(nestedScrollConnection)
    ) {
        when (isEmpty) {
            true -> {
                /** Если список пустой, показываем индикатор загрузки */
                AnimatedVisibility(
                    visible = true,
                    enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium))
                            + fadeIn(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium)),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .zIndex(0f)
                            .dynamicPadding(top = { noElementsMessageTopPadding })
                            .padding(horizontal = Dimens.uniPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center,
                        content = {
                            CircularProgressIndicator(
                                modifier = Modifier.size(52.dp),
                                color = Peach,
                                strokeWidth = 6.dp,
                            )
                        }
                    )
                }
            }
            false -> {
                /** Контент */
                content(
                    Modifier
                        .zIndex(0f)
                        .dynamicPadding(top = { spaceHeight.value.dp })
                        .padding(horizontal = Dimens.uniPadding)
                        .fillMaxSize()
                ) { bool ->
                    isEmpty = bool
                }
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////////////
        // Верхний бар
        //////////////////////////////////////////////////////////////////////////////////////////
        Box(
            modifier = Modifier
                .zIndex(1f)
                .wrapContentSize()
                .background(color = Color.Transparent)
                .onGloballyPositioned {
                    topBarHeightPx.value = it.size.height.toFloat()
                }
                .align(alignment = Alignment.TopCenter)
                .offset {
                    IntOffset(
                        x = 0,
                        y = (topBarOffsetHeightPx.value + topInsetPaddingPx).roundToInt()
                    )
                }
                .padding(horizontal = Dimens.uniPadding),
            contentAlignment = Alignment.Center
        ) {
            topBar()
        }

        ///////////////////////////////////////////////////////////////////////////////////////////
        // Плавающая кнопка
        //////////////////////////////////////////////////////////////////////////////////////////
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .wrapContentSize()
                .background(color = Color.Transparent)
                .padding(bottom = Dimens.uniPadding)
                .padding(horizontal = Dimens.uniPadding)
                .onGloballyPositioned {
                    fabWidthPx.value =
                        it.size.width.toFloat() + with(localDensity) { Dimens.uniPadding.toPx() }
                }
                .align(alignment = Alignment.BottomEnd)
                .offset {
                    IntOffset(
                        x = 0,
                        y = -(fabOffsetWidthPx.value + bottomInsetPaddingPx).roundToInt()
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            floatingButton()
        }
    }
}