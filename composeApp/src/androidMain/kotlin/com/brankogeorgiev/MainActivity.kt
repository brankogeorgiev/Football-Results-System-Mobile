package com.brankogeorgiev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.brankogeorgiev.data.network.ApiClient
import com.brankogeorgiev.data.network.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(
                client = remember {
                    ApiClient(createHttpClient(OkHttp.create()))
                }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(ApiClient(HttpClient()))
}