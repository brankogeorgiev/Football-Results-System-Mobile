package com.brankogeorgiev.data.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    var userSession by mutableStateOf<UserSession?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var loginError by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            val saved = authRepository.loadSavedSession()
            if (saved != null) {
                userSession = saved
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                loginError = null

                authRepository.login(email, password)

                userSession = authRepository.getSession()

                authRepository.saveSession(userSession)
            } catch (e: Exception) {
                loginError = e.message
            } finally {
                isLoading = false
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