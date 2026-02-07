package com.brankogeorgiev.presentation.composable.top_bar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brankogeorgiev.util.Resource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    isLoggedIn: Boolean,
    logout: () -> Unit,
    onLoginClick: () -> Unit,
    isAdmin: Boolean,
    updateIsAdmin: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.padding(horizontal = 12.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "Football",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Results System",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {},
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    painter = painterResource(Resource.Icon.TROPHY),
                    contentDescription = "Logout/Login button"
                )
            }
        },
        actions = {
            if (isLoggedIn && isAdmin) {
                IconButton(onClick = updateIsAdmin) {
                    Icon(
                        painter = painterResource(Resource.Icon.ADMIN),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            if (isLoggedIn) {
                IconButton(onClick = logout) {
                    Icon(
                        painter = painterResource(Resource.Icon.LOGOUT),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                IconButton(onClick = onLoginClick) {
                    Icon(
                        painter = painterResource(Resource.Icon.LOGIN),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun CustomTopAppBarPreview() {
    CustomTopAppBar(false, {}, {}, true, {})
}
