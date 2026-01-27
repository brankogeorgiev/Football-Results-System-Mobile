package com.brankogeorgiev.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Navigator {
    val backStack: SnapshotStateList<Screen> = mutableStateListOf(Screen.Home)

    fun goBack() {
        backStack.removeLastOrNull()
    }
}