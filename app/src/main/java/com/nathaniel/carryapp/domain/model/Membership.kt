package com.nathaniel.carryapp.domain.model

data class MembershipResponse(
    val membershipId: Long,
    val startDate: String,
    val expiryDate: String,
    val pointsBalance: Int,
    val status: String
)