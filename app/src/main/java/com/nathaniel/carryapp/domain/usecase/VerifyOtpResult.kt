package com.nathaniel.carryapp.domain.usecase

import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.domain.mapper.LoginMapper
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import javax.inject.Inject
import kotlin.math.log

sealed class VerifyOtpResult {
    data class CustomerLogin(val customerId: Long) : VerifyOtpResult()
    data class DriverLogin(val driverId: Long) : VerifyOtpResult()
    data class NewUser(val customerId: Long) : VerifyOtpResult()
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
                return when {
                    data.role == "CUSTOMER" && customerEntity?.customerId    != null -> {
                        VerifyOtpResult.CustomerLogin(
                            customerId = customerEntity.customerId
                        )
                    }

                    data.role == "DRIVER" && driverEntity?.driverId != null -> {
                        VerifyOtpResult.DriverLogin(
                            driverId = driverEntity.driverId
                        )
                    }

                    else -> VerifyOtpResult.NewUser(customerEntity!!.customerId)
                }
            }

            // If you have a Loading state in NetworkResult
            else -> VerifyOtpResult.Error("Unknown error")
        }
    }
}
