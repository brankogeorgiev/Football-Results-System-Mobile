package com.brankogeorgiev

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.session.createDataStore

fun MainViewController() = ComposeUIViewController {
    App(
        client = ApiClient(),
        prefs = remember { createDataStore() }
    )
}