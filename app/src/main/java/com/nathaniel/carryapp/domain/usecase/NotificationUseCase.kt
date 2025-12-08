package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.LocalRepository
import javax.inject.Inject

class GetAllNotificationsUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    operator fun invoke() = localRepository.getAllNotifications()
}

class GetUnreadNotificationCountUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    operator fun invoke() = localRepository.unreadNotificationCount()
}

class MarkAllNotificationsReadUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke() {
        localRepository.markAllNotificationsRead()
    }
}