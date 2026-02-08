package com.brankogeorgiev.navigation

import androidx.compose.runtime.Composable
import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.data.auth.UserSession
import com.brankogeorgiev.presentation.screen.auth.dialog.AuthMode
import com.brankogeorgiev.presentation.screen.auth.dialog.AuthUiState

@Composable
expect fun NavGraph(
    client: ApiClient,
    userSession: UserSession?,
    authenticate: (Boolean, () -> Unit) -> Unit,
    logout: () -> Unit,
    authUiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onModeChange: (AuthMode) -> Unit
)