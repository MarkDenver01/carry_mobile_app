package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.LocalRepository
import com.nathaniel.carryapp.domain.model.CartDisplayItem
import com.nathaniel.carryapp.presentation.ui.compose.orders.CartSummary
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(private val repo: LocalRepository) {
    suspend operator fun invoke(id: Long) = repo.add(id)
}

class RemoveFromCartUseCase @Inject constructor(private val repo: LocalRepository) {
    suspend operator fun invoke(id: Long) = repo.remove(id)
}

class GetCartCountUseCase @Inject constructor(private val repo: LocalRepository) {
    suspend operator fun invoke() = repo.getCartCount()
}

class GetCartSummaryUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke(): List<CartSummary> {
        return localRepository.getCartGroups()
    }
}