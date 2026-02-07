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
import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.data.auth.UserSession
import com.brankogeorgiev.presentation.composable.bottom_bar.CustomBottomAppBar
import com.brankogeorgiev.presentation.composable.top_bar.CustomTopAppBar
import com.brankogeorgiev.presentation.screen.auth.dialog.AuthDialog
import com.brankogeorgiev.presentation.screen.auth.dialog.AuthMode
import com.brankogeorgiev.presentation.screen.auth.dialog.AuthUiState
import com.brankogeorgiev.presentation.screen.home.HomeScreen
import com.brankogeorgiev.presentation.screen.players.PlayersScreen
import org.koin.compose.koinInject

@Composable
actual fun NavGraph(
    client: ApiClient,
    userSession: UserSession?,
    login: (() -> Unit) -> Unit,
    logout: () -> Unit,
    authUiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onModeChange: (AuthMode) -> Unit
) {
    val navigator = koinInject<Navigator>()
    var isLoggedIn by remember { mutableStateOf(false) }
    var isAdmin by remember { mutableStateOf(true) }
    var showAuthDialog by remember { mutableStateOf(false) }

//    val homeViewModel = HomeViewModel(client = client)
//    var homeUiState by remember { mutableStateOf(homeViewModel.uiState) }
//    val playersViewModel = PlayersViewModel(client = client)
//    var playersUiState by remember { mutableStateOf(playersViewModel.uiState) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                isLoggedIn = userSession != null,
                isAdmin = userSession?.isAdmin ?: false,
                logout = logout,
                onLoginClick = { showAuthDialog = true },
                updateIsAdmin = { isAdmin = !isAdmin }
            )
        },
        bottomBar = {
            CustomBottomAppBar(
                screen = navigator.backStack.last(),
                onNavigate = { screen ->
                    navigator.navigateToScreen(screen)
                },
                isLoggedIn = userSession != null,
                isAdmin = userSession?.isAdmin ?: false
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
                        userSession = userSession
                    )
                }
                entry<Screen.Players> {
                    PlayersScreen(
                        client = client,
                        isLoggedIn = isLoggedIn,
                        isAdmin = isAdmin,
                        userSession = userSession
                    )
                }
            }
        )
    }

    if (showAuthDialog) {
        AuthDialog(
            uiState = authUiState,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onModeChange = onModeChange,
            onSignIn = {
                login({ showAuthDialog = false })
            },
            onSignUp = {
                // TODO: signup()
                showAuthDialog = false
            },
            onDismiss = { showAuthDialog = false }
        )
    }
}