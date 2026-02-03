package com.brankogeorgiev.navigation

import androidx.compose.runtime.Composable
import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.data.auth.UserSession

@Composable
actual fun NavGraph(
    client: ApiClient,
    userSession: UserSession?,
    login: (String, String) -> Unit,
    logout: () -> Unit
) {

}