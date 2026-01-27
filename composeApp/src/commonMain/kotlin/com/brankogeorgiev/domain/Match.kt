package com.brankogeorgiev.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Match (
    val id: String = Uuid.random().toHexString(),
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int,
    val awayScore: Int,
    val matchDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)
