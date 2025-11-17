package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.AuthRepository
import com.nathaniel.carryapp.domain.mapper.LoginMapper
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import javax.inject.Inject

sealed class VerifyOtpResult {
    object CustomerLogin : VerifyOtpResult()
    object DriverLogin : VerifyOtpResult()
    object NewUser : VerifyOtpResult()
    data class Error(val message: String) : VerifyOtpResult()
}

class VerifyOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(mobile: String, otp: String): VerifyOtpResult {

        when (val result = authRepository.verifyOtp(mobile, otp)) {

            is NetworkResult.Error -> {
                return VerifyOtpResult.Error(result.message ?: "OTP verification failed")
            }

            is NetworkResult.Success -> {
                val data = result.data ?: return VerifyOtpResult.Error("Invalid server response")

                // Map to Room entities
                val loginEntity = LoginMapper.toLoginEntity(data)
                val customerEntity = LoginMapper.toCustomerEntity(data)
                val driverEntity = LoginMapper.toDriverEntity(data)

                // Save session locally
                authRepository.saveLoginSession(
                    loginEntity = loginEntity,
                    customerEntity = customerEntity,
                    driverEntity = driverEntity
                )

                return when {
                    customerEntity != null -> VerifyOtpResult.CustomerLogin
                    driverEntity != null -> VerifyOtpResult.DriverLogin
                    else -> VerifyOtpResult.NewUser
                }
            }

            else -> return VerifyOtpResult.Error("Unknown error")
        }
    }
}