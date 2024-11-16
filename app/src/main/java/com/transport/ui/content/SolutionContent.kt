package com.transport.ui.content

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.transport.model.Matrix
import com.transport.model.event.AppUIEvent
import com.transport.model.state.ScreenUIState
import com.transport.ui.component.display.DisplayMatrix
import com.transport.ui.component.assistance.RubikFontBasicText
import com.transport.ui.theme.Black
import com.transport.ui.theme.DarkGrey
import com.transport.ui.theme.Dimens.uniSpacing
import com.transport.ui.theme.Strings
import com.transport.ui.theme.TextUnits

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SolutionContent(
    modifier: Modifier,
    state: ScreenUIState,
    onEvent: (AppUIEvent) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {

    val solutions = state.solution?.first ?: emptyList()
    val result1 = state.solution?.second?.first
    val result2 = state.solution?.second?.second

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(uniSpacing)
    ) {

        if (solutions == emptyList<List<Pair<String, Matrix>>>())
            item {
                RubikFontBasicText(
                    basicMarqueeEnabled = false,
                    text = Strings.SOLUTION_NOT_FOUND,
                    style = TextStyle(
                        fontSize = TextUnits.SUB_HEADER,
                        fontWeight = FontWeight.Bold,
                        color = Black,
                    )
                )
            }

        itemsIndexed(solutions) { index, item ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(uniSpacing)
            ) {

                if (index != 0)
                    RubikFontBasicText(
                        text = Strings.STEP + "$index",
                        basicMarqueeEnabled = false,
                        style = TextStyle(
                            fontSize = TextUnits.SUB_HEADER,
                            fontWeight = FontWeight.Bold,
                            color = Black
                        )
                    )

                DisplayMatrix(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(),
                    description = item.first,
                    matrix = item.second
                )
            }
        }

        item {
            RubikFontBasicText(
                text = "Таким образом, для метода построения исходного опорного решения f = $result1",
                basicMarqueeEnabled = false,
                style = TextStyle(
                    color = DarkGrey,
                    fontSize = TextUnits.READY,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic
                )
            )
        }

        item {
            RubikFontBasicText(
                text = "А для метода потенциалов f = $result2",
                basicMarqueeEnabled = false,
                style = TextStyle(
                    color = DarkGrey,
                    fontSize = TextUnits.READY,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic
                )
            )
        }
    }
}