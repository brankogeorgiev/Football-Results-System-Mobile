package com.brankogeorgiev

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.brankogeorgiev.data.network.ApiClient
import com.brankogeorgiev.navigation.NavGraph

@Composable
@Preview
fun App(client: ApiClient) {
    MaterialTheme {
        NavGraph(client = client)
    }
}