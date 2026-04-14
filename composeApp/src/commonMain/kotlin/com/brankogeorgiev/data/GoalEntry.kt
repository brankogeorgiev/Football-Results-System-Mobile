package com.brankogeorgiev.data

import com.brankogeorgiev.data.model.Player

data class GoalEntry(
    val player: Player,
    val goals: Int = 1,
    val isOwnGoal: Boolean = false
)