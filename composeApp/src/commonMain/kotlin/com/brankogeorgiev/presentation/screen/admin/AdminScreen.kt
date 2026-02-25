package com.brankogeorgiev.presentation.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.brankogeorgiev.data.admin.AdminRepository
import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.data.auth.AuthRepository
import com.brankogeorgiev.data.model.UserWithRole
import com.brankogeorgiev.presentation.composable.LoadingIndicator
import com.brankogeorgiev.util.Resource
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AdminScreen(
    onBack: () -> Unit,
    client: ApiClient,
    authRepository: AuthRepository
) {
    val viewModel = remember {
        AdminViewModel(
            AdminRepository(
                apiClient = client,
                authRepository = authRepository
            )
        )
    }
    val state = viewModel.uiState.value

    val currentUserEmail = authRepository.getCurrentUserEmail()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item { RowBack(onBack = onBack) }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        when {
            state.isLoading -> {
                item { LoadingIndicator() }
            }

            state.error != null -> {
                item { Text(text = state.error, color = MaterialTheme.colorScheme.error) }
            }

            else -> {
                item {
                    AdminSectionCard(
                        title = "Current Admins",
                        users = state.admins,
                        isAdminSection = true,
                        updateUserRole = viewModel::updateUserRole,
                        icon = Resource.Icon.SHIELD,
                        currentUserEmail = currentUserEmail
                    )
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
                item {
                    AdminSectionCard(
                        title = "All Users",
                        users = state.users,
                        isAdminSection = false,
                        icon = Resource.Icon.MAIL,
                        updateUserRole = viewModel::updateUserRole,
                    )
                }
            }
        }
    }
}

@Composable
private fun RowBack(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(Resource.Icon.ARROW_BACK),
                contentDescription = "Back",
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Manage Admins",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun AdminSectionCard(
    title: String,
    users: List<UserWithRole>,
    isAdminSection: Boolean,
    updateUserRole: (UserWithRole) -> Unit,
    icon: DrawableResource,
    currentUserEmail: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(resource = icon),
                    contentDescription = title,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = title, style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (users.isEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isAdminSection) "No admins." else "No users.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                users.forEach { user ->
                    UserRow(
                        user = user,
                        isAdminSection = isAdminSection,
                        updateUserRole = updateUserRole,
                        currentUserEmail = currentUserEmail,
                        icon = icon
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun UserRow(
    user: UserWithRole,
    isAdminSection: Boolean,
    updateUserRole: (UserWithRole) -> Unit,
    currentUserEmail: String?,
    icon: DrawableResource
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(
                    if (isAdminSection) Color(0xFFE8F5E9)
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(resource = icon),
                    contentDescription = "",
                    tint = if (isAdminSection) Color(0xFF4CAF50)
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.email)
                Text(
                    text = if (isAdminSection) "Admin" else "User",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (isAdminSection) {
                if (user.email != currentUserEmail) {
                    IconButton(onClick = { updateUserRole(user) }) {
                        Icon(
                            painter = painterResource(Resource.Icon.ADMIN_REMOVE),
                            contentDescription = "Remove Admin",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            } else {
                IconButton(onClick = { updateUserRole(user) }) {
                    Icon(
                        painter = painterResource(Resource.Icon.ADMIN_ADD),
                        contentDescription = "Make Admin",
                        tint = Color(0xFF4CAF50)
                    )
                }
            }
        }
    }
}
