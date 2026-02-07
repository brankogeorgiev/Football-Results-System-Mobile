package com.brankogeorgiev.presentation.screen.auth.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import footballresultssystem.composeapp.generated.resources.Res
import footballresultssystem.composeapp.generated.resources.close
import footballresultssystem.composeapp.generated.resources.lock
import footballresultssystem.composeapp.generated.resources.mail
import org.jetbrains.compose.resources.painterResource

@Composable
fun AuthDialog(
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onModeChange: (AuthMode) -> Unit,
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = remember { FocusRequester() }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier.width(360.dp).wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Account",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            painter = painterResource(Res.drawable.close),
                            contentDescription = ""
                        )
                    }
                }

                AuthTabs(selected = uiState.mode, onSelect = onModeChange)

                AuthDialogInputField(
                    textField = "Email",
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    placeholder = "your@email.com",
                    icon = Res.drawable.mail,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = {
                        passwordFocusRequester.requestFocus()
                    })
                )

                AuthDialogInputField(
                    modifier = Modifier.focusRequester(passwordFocusRequester),
                    textField = "Password",
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    placeholder = "********",
                    icon = Res.drawable.lock,
                    isPassword = true,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        if (uiState.mode == AuthMode.SIGN_IN) onSignIn() else onSignUp()
                    })
                )

                LoadingMessage(isLoading = uiState.isLoading, infoMessage = uiState.infoMessage)

                if (uiState.errorMessage != null) {
                    Text(
                        modifier = Modifier.padding(start = 8.dp).fillMaxWidth()
                            .align(alignment = Alignment.CenterHorizontally),
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Button(
                    onClick = {
                        if (uiState.mode == AuthMode.SIGN_IN) onSignIn() else onSignUp()
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !uiState.isLoading
                ) {
                    Text(
                        text = if (uiState.mode == AuthMode.SIGN_IN) "Sign in" else "Create Account",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AuthDialogPreview() {
    MaterialTheme {
        AuthDialog(AuthUiState(), {}, { }, { a -> }, {}, {}, {})
    }
}
