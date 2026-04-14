package com.brankogeorgiev.presentation.composable.match

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brankogeorgiev.data.model.Player
import com.brankogeorgiev.domain.PitchSlot
import com.brankogeorgiev.domain.SlotTemplate
import kotlin.math.roundToInt

private val PitchGreen = Color(0xFF3E9142)

private val awaySlotTemplates = listOf(
    SlotTemplate("GK", 0.50f, 0.07f),
    SlotTemplate("DEF", 0.28f, 0.18f),
    SlotTemplate("DEF", 0.72f, 0.18f),
    SlotTemplate("SUB", 0.92f, 0.13f),
    SlotTemplate("MID", 0.28f, 0.31f),
    SlotTemplate("MID", 0.72f, 0.31f),
    SlotTemplate("SUB", 0.92f, 0.27f),
    SlotTemplate("FWD", 0.50f, 0.43f),
)

private val homeSlotTemplates = awaySlotTemplates.map { it.copy(x = 1f - it.x, y = 1f - it.y) }

private fun buildSlots(templates: List<SlotTemplate>, prefix: String): List<PitchSlot> =
    templates.mapIndexed { i, t ->
        PitchSlot(id = "${prefix}_$i", label = t.label, x = t.x, y = t.y)
    }

@Composable
fun FootballPitchSelection(
    homeTeamName: String,
    awayTeamName: String,
    homeTeamId: String?,
    awayTeamId: String?,
    availablePlayers: List<Player>,
    onPlayerAdded: (name: String, teamId: String?) -> Unit = { _, _ -> },
    onSlotsChanged: (homePlayers: List<Player>, awayPlayers: List<Player>) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val lineColor = Color.White.copy(alpha = 0.45f)
    val strokeWidth = 2.dp

    var awaySlots by remember { mutableStateOf(buildSlots(awaySlotTemplates, "away")) }
    var homeSlots by remember { mutableStateOf(buildSlots(homeSlotTemplates, "home")) }
    var pendingSlot by remember { mutableStateOf<PitchSlot?>(null) }

    fun allTaken(): Set<String> =
        (awaySlots + homeSlots).mapNotNull { it.player?.id }.toSet()

    fun notifyParent() = onSlotsChanged(
        homeSlots.mapNotNull { it.player },
        awaySlots.mapNotNull { it.player }
    )

    fun updateSlot(updated: PitchSlot) {
        if (updated.id.startsWith("away"))
            awaySlots = awaySlots.map { if (it.id == updated.id) updated else it }
        else
            homeSlots = homeSlots.map { if (it.id == updated.id) updated else it }
        notifyParent()
    }

    Column(modifier = modifier) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 6.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(50),
                    ambientColor = PitchGreen.copy(alpha = 0.4f),
                    spotColor = PitchGreen.copy(alpha = 0.4f)
                )
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(50))
                .padding(horizontal = 16.dp, vertical = 5.dp)
        ) {
            Text(
                text = awayTeamName.uppercase(),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                letterSpacing = 1.5.sp,
            )
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(PitchGreen)
        ) {
            val totalW = constraints.maxWidth.toFloat()
            val totalH = constraints.maxHeight.toFloat()

            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width;
                val h = size.height
                val sw = strokeWidth.toPx();
                val stroke = Stroke(sw)
                drawRect(color = lineColor, style = stroke)
                drawLine(lineColor, Offset(0f, h / 2f), Offset(w, h / 2f), sw)
                val cr = h * 0.095f
                drawCircle(lineColor, cr, Offset(w / 2f, h / 2f), style = stroke)
                drawCircle(lineColor, 4.dp.toPx(), Offset(w / 2f, h / 2f))
                val penW = w * 0.48f;
                val penH = h * 0.13f;
                val penX = (w - penW) / 2f
                drawRect(lineColor, Offset(penX, 0f), Size(penW, penH), style = stroke)
                drawRect(lineColor, Offset(penX, h - penH), Size(penW, penH), style = stroke)
                val goalW = w * 0.24f;
                val goalH = h * 0.055f;
                val goalX = (w - goalW) / 2f
                drawRect(lineColor, Offset(goalX, 0f), Size(goalW, goalH), style = stroke)
                drawRect(lineColor, Offset(goalX, h - goalH), Size(goalW, goalH), style = stroke)
                drawCircle(lineColor, 3.5.dp.toPx(), Offset(w / 2f, h * 0.175f))
                drawCircle(lineColor, 3.5.dp.toPx(), Offset(w / 2f, h * 0.825f))
            }

            val half = 24.dp
            fun slotModifier(xFrac: Float, yFrac: Float) = Modifier.absoluteOffset {
                IntOffset(
                    x = (totalW * xFrac - half.toPx()).roundToInt(),
                    y = (totalH * yFrac - half.toPx()).roundToInt()
                )
            }

            awaySlots.forEach { slot ->
                PersonSlot(
                    slot = slot,
                    isHomeTeam = false,
                    showActions = true,
                    onSlotClick = { pendingSlot = it },
                    onRemoveClick = { updateSlot(it.copy(player = null)) },
                    modifier = slotModifier(slot.x, slot.y)
                )
            }
            homeSlots.forEach { slot ->
                PersonSlot(
                    slot = slot,
                    isHomeTeam = true,
                    showActions = true,
                    onSlotClick = { pendingSlot = it },
                    onRemoveClick = { updateSlot(it.copy(player = null)) },
                    modifier = slotModifier(slot.x, slot.y)
                )
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 6.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(50),
                    ambientColor = PitchGreen.copy(alpha = 0.4f),
                    spotColor = PitchGreen.copy(alpha = 0.4f)
                )
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(50))
                .padding(horizontal = 16.dp, vertical = 5.dp)
        ) {
            Text(
                text = homeTeamName.uppercase(),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                letterSpacing = 1.5.sp,
            )
        }
    }

    pendingSlot?.let { pending ->
        val isAway = pending.id.startsWith("away")
        val live = if (isAway) awaySlots.first { it.id == pending.id }
        else homeSlots.first { it.id == pending.id }

        val takenExcludingCurrent = allTaken() - setOfNotNull(live.player?.id)

        SelectPlayerDialog(
            slot = live,
            homeTeamName = homeTeamName,
            awayTeamName = awayTeamName,
            homeTeamId = homeTeamId,
            awayTeamId = awayTeamId,
            isAwaySlot = isAway,
            allPlayers = availablePlayers,
            takenPlayerIds = takenExcludingCurrent,
            onPlayerSelected = { player ->
                updateSlot(live.copy(player = player))
                pendingSlot = null
            },
            onPlayerAdded = { name, teamId ->
                onPlayerAdded(name, teamId)
                pendingSlot = null
            },
            onDismiss = { pendingSlot = null }
        )
    }
}