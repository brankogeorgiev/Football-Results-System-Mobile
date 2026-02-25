package com.brankogeorgiev.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object Home : Screen()

    @Serializable
    object Players : Screen()

    @Serializable
    object Stats : Screen()

    @Serializable
    object Export : Screen()

    @Serializable
    object Admin : Screen()
}