package com.brankogeorgiev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.brankogeorgiev.data.auth.ApiClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(
                client = remember {
                    ApiClient()
                }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(ApiClient())
}