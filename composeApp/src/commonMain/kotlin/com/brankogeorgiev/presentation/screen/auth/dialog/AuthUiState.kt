package com.brankogeorgiev.presentation.screen.auth.dialog

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val mode: AuthMode = AuthMode.SIGN_IN,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val infoMessage: String? = null
)
