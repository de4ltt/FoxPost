package com.transport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.transport.ui.component.Header
import com.transport.ui.component.InitialDataTile
import com.transport.ui.component.Matrix
import com.transport.ui.component.MethodChoosingButton
import com.transport.ui.component.ReadyButton
import com.transport.ui.component.ScreenScaffoldWithButton
import com.transport.ui.theme.Dimens
import com.transport.ui.theme.TransportTheme
import com.transport.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
/*
        com.transport.algorithm.display()*/

        val mainViewModel: MainViewModel by viewModels()

        setContent {

            var isSwapped by rememberSaveable {
                mutableStateOf(false)
            }

            TransportTheme {
                val state by mainViewModel.screenUIState.collectAsStateWithLifecycle()

                state.apply {

                    ScreenScaffoldWithButton(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.displayCutout)
                            .padding(
                                vertical = Dimens.uniVerticalPadding,
                                horizontal = Dimens.uniPadding
                            ),
                        header = {
                            Header(title = title)
                        },
                        button = {
                            ReadyButton(
                                isActive = true,
                                onClick = { isSwapped = !isSwapped }
                            )
                        },
                        content = {
                            SharedTransitionLayout {
                                AnimatedContent(targetState = isSwapped, label = "Orange Magic") {
                                    if (!it) {
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(Dimens.uniSpacedBy)
                                        ) {
                                            MethodChoosingButton(
                                                modifier = Modifier
                                                    .sharedBounds(
                                                        rememberSharedContentState(key = "orange"),
                                                        animatedVisibilityScope = this@AnimatedContent,
                                                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(),
                                                        clipInOverlayDuringTransition = OverlayClip(
                                                            RoundedCornerShape(50)
                                                        )
                                                    )
                                            )
                                            InitialDataTile()
                                        }
                                    } else
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Matrix(
                                                mainViewModel = mainViewModel,
                                                animatedVisibilityScope = this@AnimatedContent,
                                                sharedTransitionScope = this@SharedTransitionLayout
                                            )
                                        }

                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
