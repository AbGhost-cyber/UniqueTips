package com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.models

data class VipMatchesItem(
    val leagueName: String,
    val date: String,
    val teamOne: String,
    val teamTwo: String,
    val HalfAndFullTimeScoresInOdds: String,
    val matchWon: Boolean,
    val odds: String,
    val HalfTimeScore: String,
    val FullTimeScore: String,
    val isMatchPlayed: Boolean,
    val vipName: String,
    val isCorrectScore: Boolean,
    val correctScore: String
)