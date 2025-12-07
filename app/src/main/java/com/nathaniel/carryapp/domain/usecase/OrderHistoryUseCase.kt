package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.local.room.entity.ReorderEntity
import com.nathaniel.carryapp.data.repository.LocalRepository
import javax.inject.Inject

class SaveReorderHistoryUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(items: List<ReorderEntity>) {
        localRepository.saveReorderHistory(items)
    }
}