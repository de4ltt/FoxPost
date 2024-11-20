package com.transport.ui.content

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.transport.model.Matrix
import com.transport.ui.component.display.DisplayMatrix
import com.transport.ui.component.assistance.RubikFontBasicText
import com.transport.ui.theme.Black
import com.transport.ui.theme.DarkGrey
import com.transport.ui.theme.Dimens
import com.transport.ui.theme.Dimens.uniSpacing
import com.transport.ui.theme.Strings
import com.transport.ui.theme.TextUnits
import com.transport.ui.viewmodel.Solution
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SolutionContent(
    modifier: Modifier,
    state: StateFlow<Solution>,
    emptinessChanger: (Boolean) -> Unit,
    backGesture: () -> Unit
) {

    BackHandler {
        backGesture()
        emptinessChanger(false)
        return@BackHandler
    }

    val solutions by state.collectAsStateWithLifecycle()

    emptinessChanger(false)

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(uniSpacing),
        contentPadding = PaddingValues(vertical = Dimens.uniPadding)
    ) {

/*        if (solutions == emptyList<List<Pair<String, Matrix>>>())
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
            }*/

        itemsIndexed(solutions) { index, item ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(uniSpacing)
            ) {

                if (index != 0 && item.second != null)
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
    }
}