package com.brankogeorgiev.presentation.screen.auth.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import footballresultssystem.composeapp.generated.resources.Res
import footballresultssystem.composeapp.generated.resources.logout
import footballresultssystem.composeapp.generated.resources.person

@Composable
fun AuthTabs(
    selected: AuthMode,
    onSelect: (AuthMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().background(
            // TODO: Change color
            color = Color(0xFFF1F3F5),
            shape = RoundedCornerShape(12.dp)
        ).padding(4.dp)
    ) {
        AuthTabItem(
            modifier = Modifier.weight(1f),
            text = "Sign in",
            selected = selected == AuthMode.SIGN_IN,
            icon = Res.drawable.logout,
            onClick = { onSelect(AuthMode.SIGN_IN) }
        )

        AuthTabItem(
            modifier = Modifier.weight(1f),
            text = "Sign up",
            selected = selected == AuthMode.SIGN_UP,
            icon = Res.drawable.person,
            onClick = { onSelect(AuthMode.SIGN_UP) }
        )
    }
}
