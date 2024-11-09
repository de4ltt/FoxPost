package com.transport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
                Matrix(
                    mainViewModel = mainViewModel
                )
            }
        }
    }

}