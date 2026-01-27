package com.brankogeorgiev.domain

import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Player (
    val id: String = Uuid.random().toHexString(),
    val name: String,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val defaultTeamId: Long?
)
