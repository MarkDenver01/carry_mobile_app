package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadCustomerPhotoUseCase @Inject constructor(
    private val repo: ApiRepository
) {
    suspend operator fun invoke(file: MultipartBody.Part) =
        repo.uploadCustomerPhoto(file)
}