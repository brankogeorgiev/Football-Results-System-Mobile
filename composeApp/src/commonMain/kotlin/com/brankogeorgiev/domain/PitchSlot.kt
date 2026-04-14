package com.brankogeorgiev.domain

import com.brankogeorgiev.data.model.Player

data class PitchSlot(
    val id: String,
    val label: String,
    val x: Float,
    val y: Float,
    val player: Player? = null
)