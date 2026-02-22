package com.brankogeorgiev.domain

data class TeamStanding(
    val position: Int,
    val team: String,
    val played: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: Int,
    val points: Int
)