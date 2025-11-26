package com.nathaniel.carryapp.domain.response

import java.time.LocalDateTime

data class UserHistoryResponse(
    val id: Long,
    val customerId: Long,
    val productKeyword: String,
    val dateTime: String // was LocalDateTime → now String, para walang JSON parse issue
)