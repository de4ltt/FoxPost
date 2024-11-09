package com.transport

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.animatePanBy
import androidx.compose.foundation.gestures.animateZoomBy
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.transport.ui.component.Matrix
import com.transport.ui.theme.TransportTheme
import com.transport.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val mainViewModel: MainViewModel by viewModels()

        setContent {
            TransportTheme {

                var _scale by remember { mutableFloatStateOf(1f) }
                var _offset by remember { mutableStateOf(Offset.Zero) }

                val scale by animateFloatAsState(targetValue = _scale)
                val offset by animateOffsetAsState(targetValue = _offset)

                val transformableState = rememberTransformableState { scaleChange, offsetChange, _ ->
                    _scale *= scaleChange
                    _offset += offsetChange
                }

                LaunchedEffect(!transformableState.isTransformInProgress) {
                    _scale = 1f
                    _offset = Offset.Zero
                }

                Matrix(
                    modifier = Modifier
                        .padding(vertical = 40.dp, horizontal = 40.dp)
                        .transformable(transformableState)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offset.x,
                            translationY = offset.y
                        ),
                    mainViewModel = mainViewModel
                )
            }
        }
    }

}