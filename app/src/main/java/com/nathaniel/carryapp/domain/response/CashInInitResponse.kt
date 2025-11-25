package com.nathaniel.carryapp.domain.response

data class CashInInitResponse(
    val invoiceId: String,
    val externalId: String,
    val invoiceUrl: String,
    val status: String
)