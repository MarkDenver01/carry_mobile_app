package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.model.SnowballPromo
import javax.inject.Inject

class GetSnowballPromosUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(): List<SnowballPromo> {
        return repository.getSnowballPromos()
    }
}