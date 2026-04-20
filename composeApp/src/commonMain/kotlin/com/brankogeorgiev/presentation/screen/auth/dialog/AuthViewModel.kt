package com.brankogeorgiev.presentation.screen.auth.dialog

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brankogeorgiev.data.auth.UserSession
import com.brankogeorgiev.data.repository.AuthRepository
import com.brankogeorgiev.session.SessionStorage
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sessionStorage: SessionStorage
) : ViewModel() {
    private var _uiState: MutableState<AuthUiState> = mutableStateOf(AuthUiState())
    val uiState: State<AuthUiState> = _uiState

    var userSession by mutableStateOf<UserSession?>(null)
        private set

    init {
        viewModelScope.launch {
            val active = sessionStorage.getActiveSession()
            if (active != null) {
                userSession = active
                return@launch
            }

            val saved = sessionStorage.getSavedCredentials()
            if (saved != null) {
                _uiState.value = _uiState.value.copy(
                    email = saved.email,
                    password = saved.password,
                    rememberMe = true
                )
            }
        }
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(
            email = value,
            errorMessage = null
        )
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(
            password = value,
            errorMessage = null
        )
    }

    fun onModeChange(mode: AuthMode) {
        _uiState.value = _uiState.value.copy(
            mode = mode
        )
    }

    fun onRememberMeChange(remember: Boolean) {
        _uiState.value = _uiState.value.copy(rememberMe = remember)
        if (!remember) {
            viewModelScope.launch { sessionStorage.clearSavedCredentials() }
        }
    }

    fun authenticate(isLogin: Boolean, onSuccess: () -> Unit) {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password.trim()

        val error = when {
            email.isBlank() && password.isBlank() -> "Email and password are required."
            email.isBlank() -> "Email is required."
            password.isBlank() -> "Password is required."
            else -> null
        }
        if (error != null) {
            _uiState.value = _uiState.value.copy(errorMessage = error)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                infoMessage = if (isLogin) "Signing you in..." else "Creating your account...",
                errorMessage = null
            )

            try {
                if (isLogin) authRepository.login(email = email, password = password)
                else authRepository.signUp(email = email, password = password)

                val session = authRepository.getSession()
                userSession = session

                // TODO: Check null case
                sessionStorage.saveSession(session ?: UserSession("", "", "", "", false))

                if (_uiState.value.rememberMe) {
                    sessionStorage.saveCredentials(email, password)
                } else {
                    sessionStorage.clearSavedCredentials()
                }

                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage =
                        e.message ?: if (isLogin) "Invalid credentials." else "Sign up failed.",
                    infoMessage = null
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    email = "",
                    password = ""
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionStorage.clearSession()
            userSession = null
        }
    }
}