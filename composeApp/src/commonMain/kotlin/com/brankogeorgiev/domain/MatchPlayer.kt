package com.brankogeorgiev.domain

import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class MatchPlayer (
    val id: String = Uuid.random().toHexString(),
    val matchId: String,
    val playerId: String,
    val teamId: String,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)
