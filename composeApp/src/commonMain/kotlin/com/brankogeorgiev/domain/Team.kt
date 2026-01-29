package com.brankogeorgiev.domain

import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class Team(
    val id: String = Uuid.random().toHexString(),
    val name: String,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)
