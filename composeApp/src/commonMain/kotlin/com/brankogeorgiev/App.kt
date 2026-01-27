package com.brankogeorgiev

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.brankogeorgiev.navigation.NavGraph

@Composable
@Preview
fun App() {
    MaterialTheme {
        NavGraph()
    }
}