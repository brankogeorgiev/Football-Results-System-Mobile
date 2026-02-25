package com.brankogeorgiev

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.data.auth.AuthRepository
import com.brankogeorgiev.navigation.NavGraph
import com.brankogeorgiev.presentation.screen.auth.dialog.AuthViewModel
import com.brankogeorgiev.util.darkScheme
import com.brankogeorgiev.util.lightScheme

@Composable
@Preview
fun App(client: ApiClient) {
    val colorScheme = if (isSystemInDarkTheme()) darkScheme else lightScheme

    val authRepository = remember { AuthRepository(client) }
    val authViewModel = remember { AuthViewModel(authRepository = authRepository) }

    MaterialTheme(colorScheme = colorScheme) {
        NavGraph(
            client = client,
            authRepository = authRepository,
            userSession = authViewModel.userSession,
            authenticate = authViewModel::authenticate,
            logout = authViewModel::logout,
            authUiState = authViewModel.uiState.value,
            onEmailChange = authViewModel::onEmailChange,
            onPasswordChange = authViewModel::onPasswordChange,
            onModeChange = authViewModel::onModeChange
        )
    }
}