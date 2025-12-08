package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import javax.inject.Inject

class UpdateFcmTokenOwnerUseCase @Inject constructor(
    private val apiRepository: ApiRepository
) {
    suspend operator fun invoke(token: String, customerId: Long? = null, driverId: Long? = null) {
        apiRepository.registerAndroidToken(
            token = token,
            customerId = customerId,
            driverId = driverId
        )
    }
}