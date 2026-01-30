package com.brankogeorgiev.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.brankogeorgiev.util.Resource
import org.jetbrains.compose.resources.painterResource


@Composable
fun ListItemCardActions(onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onEdit) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(Resource.Icon.EDIT),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(Resource.Icon.DELETE),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}