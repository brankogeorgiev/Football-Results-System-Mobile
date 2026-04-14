package com.brankogeorgiev.presentation.composable.match

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.brankogeorgiev.domain.PitchSlot
import com.brankogeorgiev.util.Resource
import org.jetbrains.compose.resources.painterResource

private val HomePurple = Color(0xFF7C5CBF)
private val HomeGkColor = HomePurple
private val HomeOutfieldColor = Color.White

private val AwayOutfieldColor = Color(0xFF2B2D42)
private val AwayGkColor = Color(0xFF006666)

private val SlotEmptyBg = Color.White.copy(alpha = 0.35f)
private val SlotEmptyBorder = Color.White.copy(alpha = 0.5f)

private fun slotColor(isHomeTeam: Boolean, isGk: Boolean): Color = when {
    isHomeTeam && isGk -> HomeGkColor
    isHomeTeam && !isGk -> HomeOutfieldColor
    !isHomeTeam && isGk -> AwayGkColor
    else -> AwayOutfieldColor
}

private fun iconTint(isHomeTeam: Boolean, isGk: Boolean): Color = when {
    isHomeTeam && isGk -> Color.White
    isHomeTeam && !isGk -> HomePurple
    else -> Color.White
}

/**
 * Reusable player slot composable.
 *
 * @param slot          Pitch slot data (id, label, player).
 * @param isHomeTeam    True when this slot belongs to the home side.
 * @param onSlotClick   Called on empty-slot tap, or "Change" from the popup.
 * @param onRemoveClick Called when "Remove" is chosen from the popup.
 * @param showActions   When true (pitch only) a Change/Remove popup appears on tap.
 *                      When false (everywhere else) tapping always calls [onSlotClick].
 */
@Composable
fun PersonSlot(
    slot: PitchSlot,
    isHomeTeam: Boolean,
    onSlotClick: (PitchSlot) -> Unit,
    onRemoveClick: (PitchSlot) -> Unit = {},
    showActions: Boolean = false,
    modifier: Modifier = Modifier
) {
    val hasPlayer = slot.player != null
    val isGk = slot.label == "GK"
    val fill = slotColor(isHomeTeam, isGk)
    val iconColor = iconTint(isHomeTeam, isGk)

    var showPopup by remember(slot.id) { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(48.dp)
            .clickable {
                when {
                    !hasPlayer -> onSlotClick(slot)
                    showActions -> showPopup = true
                    else -> onSlotClick(slot)
                }
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(36.dp)
        ) {

            if (hasPlayer) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(
                            elevation = if (isHomeTeam && !isGk) 3.dp else 0.dp,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .background(fill)
                        .border(1.5.dp, iconColor, CircleShape)
                )
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val radius = size.minDimension / 2f
                    val center = Offset(size.width / 2f, size.height / 2f)
                    drawCircle(color = SlotEmptyBg, radius = radius, center = center)
                    drawCircle(
                        color = SlotEmptyBorder,
                        radius = radius - 1.5.dp.toPx(),
                        center = center,
                        style = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(4.dp.toPx(), 4.dp.toPx()),
                                phase = 0f
                            )
                        )
                    )
                }
            }

            if (hasPlayer) {
                Icon(
                    painter = painterResource(Resource.Icon.PERSON),
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = "+",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlotEmptyBorder,
                    lineHeight = 20.sp
                )
            }

            if (showPopup && hasPlayer && showActions) {
                Popup(
                    alignment = Alignment.BottomCenter,
                    onDismissRequest = { showPopup = false },
                    properties = PopupProperties(focusable = true)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        tonalElevation = 4.dp,
                        shadowElevation = 8.dp,
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(12.dp))
                            .width(130.dp)
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable {
                                        showPopup = false
                                        onSlotClick(slot)
                                    }
                                    .padding(horizontal = 14.dp, vertical = 12.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Resource.Icon.REFRESH),
                                    contentDescription = "",
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
//                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Change",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable {
                                        showPopup = false
                                        onRemoveClick(slot)
                                    }
                                    .padding(horizontal = 14.dp, vertical = 12.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Resource.Icon.CLOSE),
                                    contentDescription = "",
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Remove",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = if (hasPlayer) slot.player!!.name else slot.label,
            fontSize = 8.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            lineHeight = 12.sp,
            modifier = Modifier.widthIn(max = 52.dp)
        )
    }
}