package com.transport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.transport.ui.component.Header
import com.transport.ui.component.Matrix
import com.transport.ui.component.MethodChoosingButton
import com.transport.ui.component.ReadyButton
import com.transport.ui.component.ScreenScaffoldWithButton
import com.transport.ui.theme.Dimens
import com.transport.ui.theme.TransportTheme
import com.transport.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val mainViewModel: MainViewModel by viewModels()

        setContent {
            TransportTheme {
                val state by mainViewModel.screenUIState.collectAsStateWithLifecycle()

                state.apply {

                    ScreenScaffoldWithButton(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.displayCutout)
                            .padding(
                                vertical = Dimens.uniVerticalPadding,
                                horizontal = Dimens.uniHorizontalPadding
                            ),
                        header = {
                            Header(title = title)
                        },
                        button = {
                            ReadyButton(
                                isActive = isReady,
                                onClick = { }
                            )
                        },
                        content = {
                            MethodChoosingButton()

                            Matrix(
                                mainViewModel = mainViewModel
                            )
                        }
                    )
                }
            }
        }
    }
}