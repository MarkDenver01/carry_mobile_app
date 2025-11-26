package com.nathaniel.carryapp.domain.request

import java.math.BigDecimal

data class UpdateWalletBalanceRequest(
    val mobileNumber: String,
    val amount: BigDecimal,
    val isDeduct: Boolean
)