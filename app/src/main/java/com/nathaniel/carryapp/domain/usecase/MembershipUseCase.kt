package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.model.MembershipResponse
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import javax.inject.Inject

class GetMyMembershipUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(
        customerId: Long
    ): NetworkResult<MembershipResponse> {
        return repository.getMyMembership(customerId)
    }
}

class AvailMembershipUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(customerId: Long): NetworkResult<Unit> {
        return repository.availMembership(customerId)
    }
}
