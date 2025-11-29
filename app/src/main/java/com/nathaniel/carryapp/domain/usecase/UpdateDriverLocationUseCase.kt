package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.request.DriverLocationUpdateRequest
import javax.inject.Inject

class UpdateDriverLocationUseCase @Inject constructor(
    private val apiRepository: ApiRepository
) {
    suspend operator fun invoke(request: DriverLocationUpdateRequest) =
        apiRepository.updateDriverLocation(request)
}