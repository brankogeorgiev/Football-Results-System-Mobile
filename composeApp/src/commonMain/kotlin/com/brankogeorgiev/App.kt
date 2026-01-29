package com.brankogeorgiev

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.brankogeorgiev.data.network.ApiClient
import com.brankogeorgiev.navigation.NavGraph
import com.brankogeorgiev.util.darkScheme
import com.brankogeorgiev.util.lightScheme

@Composable
@Preview
fun App(client: ApiClient) {
    val colorScheme = if (isSystemInDarkTheme()) darkScheme else lightScheme

    MaterialTheme(colorScheme = colorScheme) {
        NavGraph(client = client)
    }
}