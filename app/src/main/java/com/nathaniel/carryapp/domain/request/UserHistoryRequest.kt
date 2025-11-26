package com.nathaniel.carryapp.domain.request

import java.time.LocalDateTime

data class UserHistoryRequest(
    val customerId: Long,
    val productKeyword: String,
    // ISO string: 2025-11-26T14:34:02
    val dateTime: String
)