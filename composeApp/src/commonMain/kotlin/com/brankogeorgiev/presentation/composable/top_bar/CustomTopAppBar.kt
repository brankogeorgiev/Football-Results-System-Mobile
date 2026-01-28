package com.brankogeorgiev.presentation.composable.top_bar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    updateIsLoggedIn: () -> Unit,
    isAdmin: Boolean,
    updateIsAdmin: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.padding(horizontal = 12.dp),
        title = {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "Football",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )
                Text(
                    text = "Results System",
                    fontSize = 16.sp
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {},
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xFFADEBB3),
                    contentColor = Color(0xFF0BDA51)
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
                        contentDescription = ""
                    )
                }
            }
            if (isLoggedIn) {
                IconButton(onClick = updateIsLoggedIn) {
                    Icon(
                        painter = painterResource(Resource.Icon.LOGOUT),
                        contentDescription = ""
                    )
                }
            } else {
                IconButton(onClick = updateIsLoggedIn) {
                    Icon(
                        painter = painterResource(Resource.Icon.LOGIN),
                        contentDescription = ""
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun CustomTopAppBarPreview() {
    CustomTopAppBar(false, {}, true, {})
}
