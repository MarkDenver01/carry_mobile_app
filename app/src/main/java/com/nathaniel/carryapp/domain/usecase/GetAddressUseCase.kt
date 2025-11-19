package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.local.room.dao.DeliveryAddressDao
import com.nathaniel.carryapp.data.repository.LocalRepository
import jakarta.inject.Inject

class GetAddressUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke() = localRepository.getAddress()
}