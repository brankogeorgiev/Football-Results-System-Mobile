package com.brankogeorgiev.data

import com.brankogeorgiev.data.model.Player

data class GoalEvent(
    val player: Player,
    val isOwnGoal: Boolean
)