package com.transport.ui.component.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.res.painterResource
import com.transport.R

@Composable
fun Checks(
    modifier: Modifier = Modifier,
    color: ColorProducer,
    check: Pair<Boolean, Boolean>? = null
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        check?.let {

            if (!(it.first && it.second) && (it.first || it.second))
                Icon(
                    painter = painterResource(id = R.drawable.check),
                    tint = color.invoke(),
                    contentDescription = "",
                    modifier = Modifier.weight(1f).rotate(-90f)
                )

            if (it.second && it.first)
                Icon(
                    painter = painterResource(id = R.drawable.dcheck),
                    tint = color.invoke(),
                    contentDescription = "",
                    modifier = Modifier.weight(1f).rotate(-90f)
                )
        }
    }
}