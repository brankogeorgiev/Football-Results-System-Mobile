package com.brankogeorgiev.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.brankogeorgiev.presentation.screen.home.HomeScreen
import org.koin.compose.koinInject

@Composable
actual fun NavGraph() {
    val navigator = koinInject<Navigator>()

    NavDisplay(
        backStack = navigator.backStack,
        onBack = navigator::goBack,
        entryProvider = entryProvider {
            entry<Screen.Home>() {
                HomeScreen()
            }
        }
    )
}