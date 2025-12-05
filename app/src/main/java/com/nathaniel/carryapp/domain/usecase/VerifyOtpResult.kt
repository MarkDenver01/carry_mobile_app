package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
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
    private val apiRepository: ApiRepository
) {
    suspend operator fun invoke(mobileOrEmail: String, otp: String): VerifyOtpResult {

        return when (val result = apiRepository.verifyOtp(mobileOrEmail, otp)) {

            is NetworkResult.Error -> {
                VerifyOtpResult.Error(result.message ?: "OTP verification failed")
            }

            is NetworkResult.Success -> {
                val data = result.data
                    ?: return VerifyOtpResult.Error("Invalid server response")

                // Map to Room entities (can be nullable depending on mapper logic)
                val loginEntity = LoginMapper.toLoginEntity(data)
                val customerEntity = LoginMapper.toCustomerEntity(data)
                val driverEntity = LoginMapper.toDriverEntity(data)

                // Save session locally
                apiRepository.saveLoginSession(
                    loginEntity = loginEntity,
                    customerEntity = customerEntity,
                    driverEntity = driverEntity
                )

                // ✅ Use boolean conditions in a when { } block (not when(data.role))
                // ✅ Use driverEntity for driver branch (not customerEntity)
                when {
                    data.role == "CUSTOMER" && customerEntity != null -> {
                        VerifyOtpResult.CustomerLogin
                    }

                    data.role == "DRIVER" && driverEntity != null -> {
                        VerifyOtpResult.DriverLogin
                    }

                    else -> VerifyOtpResult.NewUser
                }
            }

            // If you have a Loading state in NetworkResult
            else -> VerifyOtpResult.Error("Unknown error")
        }
    }
}
