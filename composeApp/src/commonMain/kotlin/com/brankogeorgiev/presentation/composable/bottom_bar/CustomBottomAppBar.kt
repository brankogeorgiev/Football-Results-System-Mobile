package com.brankogeorgiev.presentation.composable.bottom_bar

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.brankogeorgiev.navigation.Screen
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomBottomAppBar(
    screen: Screen,
    onNavigate: (Screen) -> Unit,
    isAdmin: Boolean = false,
    modifier: Modifier = Modifier
) {
    val items = listOf(BottomNavItem.Results, BottomNavItem.PLayers, BottomNavItem.Stats) +
            if (isAdmin) listOf(BottomNavItem.Export) else emptyList()

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        items.forEach { item ->
            val isSelected = screen == item.screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(text = item.label)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
