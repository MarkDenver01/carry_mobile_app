package com.nathaniel.carryapp.domain.request

import java.time.LocalDateTime

data class UserHistoryRequest(
    val customerId: Long,
    val productKeyword: String,
    val dateTime: String,
)