package com.brankogeorgiev

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.data.repository.AdminRepository
import com.brankogeorgiev.data.repository.AuthRepository
import com.brankogeorgiev.navigation.NavGraph
import com.brankogeorgiev.presentation.screen.auth.dialog.AuthViewModel
import com.brankogeorgiev.session.SessionStorage
import com.brankogeorgiev.util.darkScheme
import com.brankogeorgiev.util.lightScheme

@Composable
@Preview
fun App(client: ApiClient, prefs: DataStore<Preferences>) {
    val colorScheme = if (isSystemInDarkTheme()) darkScheme else lightScheme

    val sessionStorage = remember(prefs) { SessionStorage(prefs) }
    val authRepository = remember { AuthRepository(client) }
    val adminRepository = remember { AdminRepository(client, authRepository) }
    val authViewModel =
        remember { AuthViewModel(authRepository = authRepository, sessionStorage = sessionStorage) }

    MaterialTheme(colorScheme = colorScheme) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NavGraph(
                client = client,
                authRepository = authRepository,
                adminRepository = adminRepository,
                userSession = authViewModel.userSession,
                authenticate = authViewModel::authenticate,
                logout = authViewModel::logout,
                authUiState = authViewModel.uiState.value,
                onEmailChange = authViewModel::onEmailChange,
                onPasswordChange = authViewModel::onPasswordChange,
                onModeChange = authViewModel::onModeChange,
                onRememberMeChange = authViewModel::onRememberMeChange
            )
        }
    }
}