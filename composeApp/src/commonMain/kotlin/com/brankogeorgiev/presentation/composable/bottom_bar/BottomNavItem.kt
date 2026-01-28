package com.brankogeorgiev.presentation.composable.bottom_bar

import com.brankogeorgiev.navigation.Screen
import com.brankogeorgiev.util.Resource
import org.jetbrains.compose.resources.DrawableResource

enum class BottomNavItem(val screen: Screen, val label: String, val icon: DrawableResource) {
    Results(Screen.Home, "Results", Resource.Icon.TROPHY),
    PLayers(Screen.Players, "Players", Resource.Icon.PLAYERS),
    Stats(Screen.Stats, "Stats", Resource.Icon.STATS),
    Export(Screen.Export, "Export", Resource.Icon.EXPORT)
}