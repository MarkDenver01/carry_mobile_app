package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.LocalRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(private val repo: LocalRepository) {
    suspend operator fun invoke(id: Long) = repo.add(id)
}

class RemoveFromCartUseCase @Inject constructor(private val repo: LocalRepository) {
    suspend operator fun invoke(id: Long) = repo.remove(id)
}

class GetCartSummaryUseCase @Inject constructor(private val repo: LocalRepository) {
    suspend operator fun invoke() = repo.getSummary()
}

class GetCartTotalUseCase @Inject constructor(private val repo: LocalRepository) {
    suspend operator fun invoke() = repo.getTotal()
}

class GetCartCountUseCase @Inject constructor(private val repo: LocalRepository) {
    suspend operator fun invoke() = repo.getCartCount()
}