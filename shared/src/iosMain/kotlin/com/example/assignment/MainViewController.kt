package com.example.assignment

import androidx.compose.ui.window.ComposeUIViewController
import com.example.assignment.di.initKoin

private var isKoinStarted = false

fun MainViewController() = ComposeUIViewController {
    if (!isKoinStarted) {
        initKoin()
        isKoinStarted = true
    }
    App()
}
