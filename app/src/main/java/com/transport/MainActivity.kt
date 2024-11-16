package com.transport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.transport.model.event.AppUIEvent
import com.transport.model.state.ScreenMode
import com.transport.ui.algorythm.launchMethod
import com.transport.ui.component.assistance.Header
import com.transport.ui.component.assistance.ReadyButton
import com.transport.ui.component.assistance.ScreenScaffoldWithButton
import com.transport.ui.content.DefaultMainContent
import com.transport.ui.content.InitialDataContent
import com.transport.ui.content.SolutionContent
import com.transport.ui.theme.Dimens
import com.transport.ui.theme.TransportTheme
import com.transport.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        launchMethod()

        val mainViewModel: MainViewModel by viewModels()

        val onEvent = mainViewModel::onEvent

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
                                onClick = {
                                    onEvent(
                                        AppUIEvent.FindSolution
                                    )
                                    onEvent(
                                        AppUIEvent.ChangeScreenMode(
                                            ScreenMode.SOLUTION
                                        )
                                    )
                                }
                            )
                        },
                        content = {

                            SharedTransitionLayout {
                                AnimatedContent(targetState = screenMode, label = "Orange Magic") {

                                    when (it) {
                                        ScreenMode.DEFAULT -> {
                                            DefaultMainContent(
                                                modifier = Modifier.weight(1f),
                                                onEvent = mainViewModel::onEvent,
                                                method = state.solutionMode,
                                                animatedVisibilityScope = this@AnimatedContent,
                                                sharedTransitionScope = this@SharedTransitionLayout
                                            )
                                        }

                                        ScreenMode.INITIAL_DATA -> {
                                            InitialDataContent(
                                                modifier = Modifier.fillMaxSize(),
                                                mainViewModel = mainViewModel,
                                                animatedVisibilityScope = this@AnimatedContent,
                                                sharedTransitionScope = this@SharedTransitionLayout
                                            )
                                        }

                                        ScreenMode.LOADING_SOLUTION -> {

                                        }

                                        ScreenMode.SOLUTION -> {
                                            SolutionContent(
                                                modifier = Modifier.fillMaxSize(),
                                                state = state,
                                                onEvent = mainViewModel::onEvent,
                                                animatedVisibilityScope = this@AnimatedContent,
                                                sharedTransitionScope = this@SharedTransitionLayout
                                            )
                                        }
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