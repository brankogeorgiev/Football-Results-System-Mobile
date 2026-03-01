package com.brankogeorgiev.presentation.screen.admin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brankogeorgiev.data.model.UserWithRole
import com.brankogeorgiev.data.repository.AdminRepository
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repository: AdminRepository
) : ViewModel() {
    private val _uiState: MutableState<AdminUiState> = mutableStateOf(AdminUiState())
    var uiState: State<AdminUiState> = _uiState

    init {
        loadUsers()
    }

    fun updateUserRole(user: UserWithRole) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                if (user.role == "user") {
                    repository.updateUserRole(id = user.roleId, role = "admin")
                    loadUsers()
                } else {
                    repository.updateUserRole(id = user.roleId, role = "user")
                    loadUsers()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Error updating user role.")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val users = repository.getUsers()
                _uiState.value = _uiState.value.copy(isLoading = false, allUsers = users)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Something went wrong"
                )
            }
        }
    }
}