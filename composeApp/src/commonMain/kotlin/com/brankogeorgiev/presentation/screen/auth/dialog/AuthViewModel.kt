package com.brankogeorgiev.presentation.screen.auth.dialog

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brankogeorgiev.data.auth.AuthRepository
import com.brankogeorgiev.data.auth.UserSession
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private var _uiState: MutableState<AuthUiState> = mutableStateOf(AuthUiState())
    val uiState: State<AuthUiState> = _uiState

    var userSession by mutableStateOf<UserSession?>(null)
        private set

    init {
        viewModelScope.launch {
            val saved = authRepository.loadSavedSession()
            if (saved != null) {
                userSession = saved
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

    fun authenticate(isLogin: Boolean, onSuccess: () -> Unit) {
        val email = _uiState.value.email
        val password = _uiState.value.password

        val emailEmpty = email.trim().isBlank()
        val passwordEmpty = password.trim().isBlank()

        if (emailEmpty && passwordEmpty) {
            _uiState.value = _uiState.value.copy(errorMessage = "Email and password are required.")
            return
        } else if (emailEmpty) {
            _uiState.value = _uiState.value.copy(errorMessage = "Email is required.")
            return
        } else if (passwordEmpty) {
            _uiState.value = _uiState.value.copy(errorMessage = "Password is required.")
            return
        }

        val infoText = if (isLogin) "Signing you in..." else "Creating your account..."

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    infoMessage = infoText,
                    errorMessage = null
                )

                if (isLogin) authRepository.login(email = email, password = password)
                else authRepository.signUp(email = email, password = password)

                userSession = authRepository.getSession()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                        ?: if (isLogin) "Invalid credentials" else "Sign up failed. Please try again.",
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
            userSession = null
            authRepository.clearSavedSession()
        }
    }
}