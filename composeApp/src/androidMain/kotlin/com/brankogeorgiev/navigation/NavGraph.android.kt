package com.brankogeorgiev.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.brankogeorgiev.presentation.composable.CustomTopAppBar
import com.brankogeorgiev.presentation.screen.home.HomeScreen
import org.koin.compose.koinInject

@Composable
actual fun NavGraph() {
    val navigator = koinInject<Navigator>()
    var isLoggedIn by remember { mutableStateOf(false) }
    var isAdmin by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                isLoggedIn = isLoggedIn,
                updateIsLoggedIn = { isLoggedIn = !isLoggedIn },
                isAdmin = isAdmin,
                updateIsAdmin = { isAdmin = !isAdmin }
            )
        }
    ) { paddingValues ->
        NavDisplay(
            modifier = Modifier.padding(paddingValues),
            backStack = navigator.backStack,
            onBack = navigator::goBack,
            entryProvider = entryProvider {
                entry<Screen.Home>() {
                    HomeScreen(
                        isLoggedIn = isLoggedIn,
                        updateIsLoggedIn = { isLoggedIn = !isLoggedIn },
                        isAdmin = isAdmin
                    )
                }
            }
        )
    }
}