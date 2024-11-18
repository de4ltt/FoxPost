package com.transport

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.transport.model.event.AppUIEvent
import com.transport.model.state.ScreenMode
import com.transport.ui.component.assistance.Header
import com.transport.ui.component.assistance.ListWithTopAndFab
import com.transport.ui.component.assistance.ReadyButton
import com.transport.ui.content.DefaultMainContent
import com.transport.ui.content.InitialDataContent
import com.transport.ui.content.SolutionContent
import com.transport.ui.theme.TransportTheme
import com.transport.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.hideSystemUi(extraAction = {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        })
        setDisplayCutoutMode()

        val mainViewModel: MainViewModel by viewModels()

        val onEvent = mainViewModel::onEvent

        setContent {
            TransportTheme {

                val state by mainViewModel.screenUIState.collectAsStateWithLifecycle()
                val _solution = mainViewModel.solution

                state.apply {

                    ListWithTopAndFab(
                        topBar = {
                            Header(title = title)
                        },
                        floatingButton = {
                            ReadyButton(
                                isActive = isReady,
                                onClick = {
                                    if (mainViewModel.screenUIState.value.screenMode != ScreenMode.SOLUTION) {
                                        onEvent(
                                            AppUIEvent.FindSolution
                                        )
                                        onEvent(
                                            AppUIEvent.ChangeScreenMode(
                                                ScreenMode.SOLUTION
                                            )
                                        )
                                    }
                                },
                                backTap = {
                                    if (mainViewModel.screenUIState.value.screenMode == ScreenMode.SOLUTION) {
                                        mainViewModel.onEvent(
                                            AppUIEvent.ChangeScreenMode(
                                                ScreenMode.INITIAL_DATA
                                            )
                                        )
                                        mainViewModel.onEvent(
                                            AppUIEvent.IdleSolution
                                        )
                                    }
                                }
                            )
                        }
                    ) { modifier, emptinessChanger ->
                        Box(modifier = modifier) {
                            SharedTransitionLayout {
                                AnimatedContent(targetState = screenMode, label = "Orange Magic") { mode ->

                                    when (mode) {
                                        ScreenMode.DEFAULT -> {
                                            DefaultMainContent(
                                                modifier = Modifier.fillMaxSize(),
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

                                        ScreenMode.SOLUTION -> {
                                            SolutionContent(
                                                modifier = Modifier.fillMaxSize(),
                                                state = mainViewModel.solution,
                                                backGesture = {
                                                    mainViewModel.onEvent(
                                                        AppUIEvent.ChangeScreenMode(
                                                            ScreenMode.INITIAL_DATA
                                                        )
                                                    )
                                                    mainViewModel.onEvent(
                                                        AppUIEvent.IdleSolution
                                                    )
                                                },
                                                emptinessChanger = emptinessChanger
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

//                    ScreenScaffoldWithButton(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .windowInsetsPadding(WindowInsets.displayCutout)
//                            .padding(
//                                vertical = Dimens.uniVerticalPadding,
//                                horizontal = Dimens.uniHorizontalPadding
//                            ),
//                        header = {
//                            Header(title = title)
//                        },
//                        button = {
//                            ReadyButton(
//                                isActive = isReady,
//                                onClick = {
//                                    onEvent(
//                                        AppUIEvent.FindSolution
//                                    )
//                                    onEvent(
//                                        AppUIEvent.ChangeScreenMode(
//                                            ScreenMode.SOLUTION
//                                        )
//                                    )
//                                },
//                                backTap = {
//                                    if (mainViewModel.screenUIState.value.screenMode == ScreenMode.SOLUTION)
//                                        mainViewModel.onEvent(
//                                            AppUIEvent.ChangeScreenMode(
//                                                ScreenMode.DEFAULT
//                                            )
//                                        )
//                                }
//                            )
//                        },
//                        content = {
//
//                            SharedTransitionLayout {
//                                AnimatedContent(targetState = screenMode, label = "Orange Magic") {
//
//                                    when (it) {
//                                        ScreenMode.DEFAULT -> {
//                                            DefaultMainContent(
//                                                modifier = Modifier.weight(1f),
//                                                onEvent = mainViewModel::onEvent,
//                                                method = state.solutionMode,
//                                                animatedVisibilityScope = this@AnimatedContent,
//                                                sharedTransitionScope = this@SharedTransitionLayout
//                                            )
//                                        }
//
//                                        ScreenMode.INITIAL_DATA -> {
//                                            InitialDataContent(
//                                                modifier = Modifier.fillMaxSize(),
//                                                mainViewModel = mainViewModel,
//                                                animatedVisibilityScope = this@AnimatedContent,
//                                                sharedTransitionScope = this@SharedTransitionLayout
//                                            )
//                                        }
//
//                                        ScreenMode.SOLUTION -> {
//                                            SolutionContent(
//                                                modifier = Modifier.fillMaxSize(),
//                                                state = state,
//                                                onEvent = mainViewModel::onEvent,
//                                                animatedVisibilityScope = this@AnimatedContent,
//                                                sharedTransitionScope = this@SharedTransitionLayout,
//                                                backGesture = {
//                                                    mainViewModel.onEvent(
//                                                        AppUIEvent.ChangeScreenMode(
//                                                            ScreenMode.DEFAULT
//                                                        )
//                                                    )
//                                                }
//                                            )
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    )
                }
            }
        }
    }
}

private fun Window.hideSystemUi(extraAction:(WindowInsetsControllerCompat.() -> Unit)? = null) {
    WindowInsetsControllerCompat(this, this.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        extraAction?.invoke(controller)
    }
}

private fun Activity.setDisplayCutoutMode() {
    when {
        Build.VERSION.SDK_INT < Build.VERSION_CODES.R -> {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
        }
    }
}