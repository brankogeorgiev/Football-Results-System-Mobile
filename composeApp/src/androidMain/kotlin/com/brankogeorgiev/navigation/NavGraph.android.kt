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
import com.brankogeorgiev.data.network.ApiClient
import com.brankogeorgiev.presentation.composable.bottom_bar.CustomBottomAppBar
import com.brankogeorgiev.presentation.composable.top_bar.CustomTopAppBar
import com.brankogeorgiev.presentation.screen.home.HomeScreen
import com.brankogeorgiev.presentation.screen.players.PlayersScreen
import org.koin.compose.koinInject

@Composable
actual fun NavGraph(client: ApiClient) {
    val navigator = koinInject<Navigator>()
    var isLoggedIn by remember { mutableStateOf(false) }
    var isAdmin by remember { mutableStateOf(true) }

//    val homeViewModel = HomeViewModel(client = client)
//    var homeUiState by remember { mutableStateOf(homeViewModel.uiState) }
//    val playersViewModel = PlayersViewModel(client = client)
//    var playersUiState by remember { mutableStateOf(playersViewModel.uiState) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                isLoggedIn = isLoggedIn,
                updateIsLoggedIn = { isLoggedIn = !isLoggedIn },
                isAdmin = isAdmin,
                updateIsAdmin = { isAdmin = !isAdmin }
            )
        },
        bottomBar = {
            CustomBottomAppBar(
                screen = navigator.backStack.last(),
                onNavigate = { screen ->
                    navigator.navigateToScreen(screen)
                },
                isLoggedIn = isLoggedIn,
                isAdmin = isAdmin
            )
        }
    ) { paddingValues ->
        NavDisplay(
            modifier = Modifier.padding(paddingValues),
            backStack = navigator.backStack,
            onBack = navigator::goBack,
            entryProvider = entryProvider {
                entry<Screen.Home> {
                    HomeScreen(
                        client = client,
                        isLoggedIn = isLoggedIn,
                        isAdmin = isAdmin,
                    )
                }
                entry<Screen.Players> {
                    PlayersScreen(
                        client = client,
                        isLoggedIn = isLoggedIn,
                        isAdmin = isAdmin
                    )
                }
            }
        )
    }
}