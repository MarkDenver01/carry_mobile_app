package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.local.room.entity.DeliveryAddressEntity
import com.nathaniel.carryapp.data.repository.LocalRepository
import jakarta.inject.Inject

class SaveAddressUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(address: DeliveryAddressEntity) {
        localRepository.saveAddress(address)
    }
}