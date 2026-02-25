package com.brankogeorgiev.presentation.screen.admin

import com.brankogeorgiev.data.model.UserWithRole

data class AdminUiState(
    val isLoading: Boolean = false,
    val allUsers: List<UserWithRole> = emptyList(),
    val error: String? = null
) {
    val admins: List<UserWithRole>
        get() = allUsers.filter { it.role == "admin" }

    val users: List<UserWithRole>
        get() = allUsers.filter { it.role == "user" }
}
